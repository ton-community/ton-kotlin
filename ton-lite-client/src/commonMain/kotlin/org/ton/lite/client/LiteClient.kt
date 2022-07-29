package org.ton.lite.client

import io.ktor.utils.io.core.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.ton.adnl.client.AdnlTcpClient
import org.ton.adnl.client.AdnlTcpClientImpl
import org.ton.api.exception.TonNotReadyException
import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.tonnode.*
import org.ton.block.*
import org.ton.boc.BagOfCells
import org.ton.crypto.encodeHex
import org.ton.crypto.sha256
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerBlockData
import org.ton.lite.api.liteserver.LiteServerMasterchainInfoExt
import org.ton.lite.api.liteserver.LiteServerVersion
import org.ton.lite.api.liteserver.functions.LiteServerRunSmcMethod
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import org.ton.tlb.parse
import kotlin.math.max
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun LiteClient(block: LiteClientConfig.() -> Unit) = LiteClient(LiteClientConfig(block))

open class LiteClient(
    val adnl: AdnlTcpClient,
    val config: LiteClientConfig = LiteClientConfig(),
    private val logger: Logger = PrintLnLogger("TON LiteClient")
) : Closeable {
    constructor(config: LiteClientConfig, logger: Logger = PrintLnLogger("TON LiteClient")) : this(
        AdnlTcpClientImpl(config.ipv4, config.port, PublicKeyEd25519(config.publicKey)),
        config,
        logger
    )

    val liteApi: LiteApi = LiteApi {
        adnl.sendQuery(it)
    }

    private var isInit: Boolean = false
    private val knownBlockIds: ArrayDeque<TonNodeBlockIdExt> = ArrayDeque(100)
    private var lastMasterchainBlockId: TonNodeBlockIdExt by atomic(TonNodeBlockIdExt())
    private var zeroStateId: TonNodeZeroStateIdExt by atomic(TonNodeZeroStateIdExt())
    private var lastBlock: LiteServerBlockData? by atomic(null)
    var serverVersion: Int by atomic(0)
        private set
    var serverCapabilities: Long by atomic(0L)
        private set
    var serverTime: Instant by atomic(Clock.System.now())
        private set
    var serverTimeGotAt: Instant by atomic(Clock.System.now())
        private set

    fun latency(): Duration = serverTimeGotAt - serverTime

    fun setServerVersion(version: Int, capabilities: Long) {
        if (serverVersion != version || serverCapabilities != capabilities) {
            serverVersion = version
            serverCapabilities = capabilities
            logger.info { "server version is ${version shr 8}.${serverVersion and 0xFF}, capabilities $serverCapabilities" }
        }
    }

    fun setServerTime(time: Int): Duration {
        serverTime = Instant.fromEpochSeconds(time.toLong())
        serverTimeGotAt = Clock.System.now()
        val latency = latency()
        logger.debug { "server time is $serverTime (latency $latency)" }
        return latency
    }

    override fun close() = runBlocking {
        knownBlockIds.clear()
        adnl.disconnect()
        isInit = false
    }

    suspend fun start(input: Boolean = false) {
        init()

        if (input) {
            while (true) {
                try {
                    parseLine()
                } catch (e: Exception) {
                    logger.fatal { e.toString() }
                }
            }
        }
    }

    private suspend fun init() {
        if (isInit) return
        isInit = true
        try {
            adnl.connect()
            getServerVersion()
            getLastBlockId()
        } catch (e: Exception) {
            isInit = false
            throw e
        }
    }

    suspend fun getServerVersion(): LiteServerVersion {
        init()
        val version = try {
            liteApi.getVersion()
        } catch (e: Exception) {
            throw RuntimeException("Can't get server version and time", e)
        }
        setServerVersion(version.version, version.capabilities)
        return version
    }

    suspend fun getLastBlockId(mode: Int = if (serverCapabilities and 2 != 0L) 0 else -1): TonNodeBlockIdExt {
        init()
        val masterchainInfo = try {
            if (mode < 0) {
                liteApi.getMasterchainInfo()
            } else {
                liteApi.getMasterchainInfoExt(mode)
            }
        } catch (e: Exception) {
            throw RuntimeException("Can't get masterchain info from server", e)
        }
        val blockId = masterchainInfo.last

        logger.debug { "last masterchain block is $blockId" }

        var createdAt: Instant? = null
        if (masterchainInfo is LiteServerMasterchainInfoExt) {
            setServerVersion(masterchainInfo.version, masterchainInfo.capabilities)
            setServerTime(masterchainInfo.now)
            val serverNow = Instant.fromEpochSeconds(masterchainInfo.now.toLong())
            val lastUtime = Instant.fromEpochSeconds(masterchainInfo.last_utime.toLong())
            createdAt = lastUtime
            if (masterchainInfo.last_utime > masterchainInfo.now) {
                logger.warn {
                    "server claims to have a masterchain block $blockId created at $lastUtime " +
                            "(${lastUtime - serverNow} in future)"
                }
            } else if (lastUtime < serverNow - 60.seconds) {
                logger.warn {
                    "server appears to be out of sync: its newest masterchain block is $blockId" +
                            " created at $lastUtime (${serverNow - lastUtime} ago according to the server's clock)"
                }
            } else if (lastUtime < serverTimeGotAt - 60.seconds) {
                logger.warn {
                    "either the server is out of sync, or the local clock is set incorrectly: the newest masterchain " +
                            "block known to server is $blockId created at $lastUtime " +
                            "(${serverNow - serverTimeGotAt} ago according to the local clock)"
                }
            }
        }

        val currentZeroStateId = zeroStateId
        if (!currentZeroStateId.isValid()) {
            zeroStateId = masterchainInfo.init
            logger.info { "zero state id set to ${masterchainInfo.init}" }
        } else if (masterchainInfo.init != currentZeroStateId) {
            use {
                throw IllegalStateException("masterchain zero state id suddenly changed: expected $zeroStateId, actual ${masterchainInfo.init}")
            }
        }
        registerBlockId(blockId)
        registerBlockId(
            TonNodeBlockIdExt(
                Workchain.MASTERCHAIN_ID,
                Shard.ID_ALL,
                0,
                zeroStateId.root_hash,
                zeroStateId.file_hash
            )
        )
        if (!lastMasterchainBlockId.isValid()) {
            lastMasterchainBlockId = blockId
        } else if (lastMasterchainBlockId.seqno < masterchainInfo.last.seqno) {
            lastMasterchainBlockId = blockId
        }
        logger.debug {
            "latest masterchain block known to server is:\n$blockId${
                if (createdAt != null) {
                    "\n  created at $createdAt (${Clock.System.now() - createdAt} ago)"
                } else ""
            }"
        }
        return blockId
    }

    suspend fun lookupBlock(blockId: TonNodeBlockId, timeout: Duration) = withTimeoutOrNull(timeout) {
        var result: TonNodeBlockIdExt? = null
        while (isActive && result == null) {
            result = lookupBlock(blockId)
        }
        result
    }

    suspend fun lookupBlock(blockId: TonNodeBlockId? = null): TonNodeBlockIdExt? {
        init()
        if (blockId == null) {
            return getLastBlockId()
        }
        if (blockId is TonNodeBlockIdExt) return blockId
        val knownBlockId = knownBlockIds.find { it == blockId }
        if (knownBlockId != null) {
            return knownBlockId
        }
        val blockHeader = try {
            liteApi.lookupBlock(blockId)
        } catch (e: TonNotReadyException) {
            return null
        } catch (e: Exception) {
            throw RuntimeException("Can't lookup block header for $blockId from server", e)
        }
        val actualBlockId = blockHeader.id
        check((!blockId.isValid()) || blockId == actualBlockId) {
            "block id mismatch, expected: $blockId actual: $actualBlockId"
        }
        registerBlockId(blockHeader.id)
        return blockHeader.id
    }

    suspend fun getBlock(blockId: TonNodeBlockIdExt, timeout: Duration) = withTimeoutOrNull(timeout) {
        var result: Block? = null
        while (isActive && result == null) {
            result = getBlock(blockId)
        }
        result
    }

    suspend fun getBlock(blockId: TonNodeBlockIdExt): Block? {
        init()
        val blockData = try {
            liteApi.getBlock(blockId)
        } catch (e: TonNotReadyException) {
            return null
        } catch (e: Exception) {
            throw RuntimeException("Can't get block $blockId from server", e)
        }
        logger.debug { "got ${blockData.data.size} data bytes for block $blockId" }
        val actualFileHash = sha256(blockData.data)
        check(blockId.file_hash.contentEquals(actualFileHash)) {
            "file hash mismatch for block $blockId, expected: ${
                blockId.file_hash.encodeHex().uppercase()
            } , actual: ${actualFileHash.encodeHex().uppercase()}"
        }
        registerBlockId(blockId)
        val root = try {
            blockData.dataBagOfCells().first()
        } catch (e: Exception) {
            throw RuntimeException("Can't deserialize block data", e)
        }
        // TODO: Fix root hash calculation (Pruned branch cells?)
//        val actualRootHash = root.hash()
//        check(blockId.root_hash.contentEquals(actualRootHash)) {
//            "block root hash mismatch, expected: ${
//                blockId.root_hash.encodeHex().uppercase()
//            } , actual: ${actualRootHash.encodeHex().uppercase()}"
//        }
        val block = try {
            root.parse(Block)
        } catch (e: Exception) {
            throw RuntimeException("Can't parse block: $blockId", e)
        }
        val shard = block.info.shard
        val prevs: List<TonNodeBlockIdExt>
        val prevSeqno: Int
        if (!block.info.after_merge) {
            val prev1 = block.info.prev_ref.prevs().first()
            prevSeqno = prev1.seq_no
            val prevBlockIdExt = TonNodeBlockIdExt(
                shard.workchain_id,
                if (block.info.after_split) Shard.shardParent(shard.shard_prefix.toULong())
                    .toLong() else shard.shard_prefix,
                prev1.seq_no, prev1.root_hash, prev1.file_hash
            )
            check(!block.info.after_split || prev1.seq_no != 0) {
                "shardchains cannot be split immediately after initial state"
            }
            prevs = listOf(prevBlockIdExt)
        } else {
            check(!block.info.after_split) {
                "shardchains cannot be simultaneously split and merged at the same block"
            }
            val (prev1, prev2) = block.info.prev_ref.prevs()
            prevSeqno = max(prev1.seq_no, prev2.seq_no)
            prevs = listOf(
                TonNodeBlockIdExt(
                    shard.workchain_id,
                    Shard.shardChild(shard.shard_prefix.toULong(), true).toLong(),
                    prev1.seq_no,
                    prev1.root_hash,
                    prev1.file_hash
                ),
                TonNodeBlockIdExt(
                    shard.workchain_id,
                    Shard.shardChild(shard.shard_prefix.toULong(), false).toLong(),
                    prev2.seq_no,
                    prev2.root_hash,
                    prev2.file_hash
                ),
            )
            check(prev1.seq_no != 0 && prev2.seq_no != 0) {
                "shardchains cannot be merged immediately after initial state"
            }
        }
        check(block.info.seq_no == prevSeqno + 1) {
            "block $blockId has invalid seqno, expected: ${prevSeqno + 1} , actual: ${block.info.seq_no}"
        }
        prevs.forEachIndexed { index, id ->
            logger.debug { "previous block #$index : $id" }
        }
        val mcBlockId = if (shard.workchain_id == Workchain.MASTERCHAIN_ID) {
            prevs.first()
        } else {
            val mcRef = checkNotNull(block.info.master_ref?.master) { "missing master block reference" }
            TonNodeBlockIdExt(Workchain.MASTERCHAIN_ID, Shard.ID_ALL, mcRef.seq_no, mcRef.root_hash, mcRef.file_hash)
        }
        logger.debug { "reference masterchain block: $mcBlockId" }
        registerBlockId(mcBlockId)
        return block
    }

    suspend fun getAccount(
        address: String,
        mode: Int = 0
    ): AccountInfo? = getAccount(parseAccountId(address), mode)

    suspend fun getAccount(
        address: LiteServerAccountId?,
        mode: Int = 0
    ): AccountInfo? {
        init()
        return getAccount(address, lastMasterchainBlockId, mode)
    }

    suspend fun getAccount(
        address: String?,
        blockId: TonNodeBlockIdExt,
        mode: Int = 0
    ): AccountInfo? = getAccount(address?.let { parseAccountId(address) }, blockId, mode)

    suspend fun getAccount(
        address: LiteServerAccountId?,
        blockId: TonNodeBlockIdExt,
        mode: Int = 0
    ): AccountInfo? {
        init()
        if (address == null) return null
        logger.debug {
            "requesting account state for ${address.workchain}:${
                address.id.encodeHex().uppercase()
            } with respect to $blockId with mode $mode"
        }
        val accountState = liteApi.getAccountState(blockId, address)
        val shardBlock = accountState.shard_blk
        logger.debug {
            "got account state for ${address.workchain}:${
                address.id.encodeHex().uppercase()
            } with respect to blocks ${blockId}${if (shardBlock == blockId) "" else " and $shardBlock"}"
        }
        val stateBoc = accountState.stateBagOfCells()
        return stateBoc.first().parse(Account) as? AccountInfo
    }

    suspend fun runSmcMethod(
        address: LiteServerAccountId,
        methodName: String,
        vararg params: VmStackValue
    ): VmStack {
        init()
        logger.debug { "run: $address - ${params.toList()}" }
        return runSmcMethod(
            address,
            lastMasterchainBlockId,
            LiteServerRunSmcMethod.methodId(methodName),
            params.asIterable()
        )
    }

    suspend fun runSmcMethod(
        address: LiteServerAccountId,
        method: Long,
        vararg params: VmStackValue
    ): VmStack {
        init()
        return runSmcMethod(address, lastMasterchainBlockId, method, params.asIterable())
    }

    suspend fun runSmcMethod(
        address: LiteServerAccountId,
        methodName: String,
        params: Iterable<VmStackValue>
    ): VmStack {
        init()
        return runSmcMethod(address, lastMasterchainBlockId, LiteServerRunSmcMethod.methodId(methodName), params)
    }

    suspend fun runSmcMethod(
        address: LiteServerAccountId,
        method: Long,
        params: Iterable<VmStackValue>
    ): VmStack {
        init()
        return runSmcMethod(address, lastMasterchainBlockId, method, params)
    }

    suspend fun runSmcMethod(
        address: LiteServerAccountId,
        blockId: TonNodeBlockIdExt,
        methodName: String,
        vararg params: VmStackValue
    ) = runSmcMethod(address, blockId, LiteServerRunSmcMethod.methodId(methodName), *params)

    suspend fun runSmcMethod(
        address: LiteServerAccountId,
        blockId: TonNodeBlockIdExt,
        methodName: String,
        params: Iterable<VmStackValue>
    ) = runSmcMethod(address, blockId, LiteServerRunSmcMethod.methodId(methodName), params)

    suspend fun runSmcMethod(
        address: LiteServerAccountId,
        blockId: TonNodeBlockIdExt,
        method: Long,
        vararg params: VmStackValue
    ) = runSmcMethod(address, blockId, method, params.asIterable())

    suspend fun runSmcMethod(
        address: LiteServerAccountId,
        blockId: TonNodeBlockIdExt,
        method: Long,
        params: Iterable<VmStackValue>
    ): VmStack {
        init()
        val result = liteApi.runSmcMethod(0b100, blockId, address, method, params)
        check((!blockId.isValid()) || blockId == result.id) {
            "block id mismatch, expected: $blockId actual: $result.id"
        }
        // TODO: check proofs
        val exitCode = result.exitCode
        val stack = result.result?.let {
            try {
                BagOfCells(it).first().parse(VmStack)
            } catch (e: Exception) {
                throw RuntimeException("Can't parse result for $method@$address($params)", e)
            }
        } ?: throw IllegalStateException("exit code: $exitCode")
        return stack
    }

    suspend fun resolveDns(domain: String, category: Int = 0) {
        init()
        return resolveDns(lastMasterchainBlockId, domain, category)
    }

    suspend fun resolveDns(blockId: TonNodeBlockIdExt, domain: String, category: Int = 0) {
        require(domain.length <= 1023) { "domain name too long" }
        domain.forEach { char ->
            require(char.code < 0xfe && char > ' ') {
                "invalid characters in a domain name"
            }
        }
    }

    private suspend fun parseLine() {
        val words = readln().split(" ")
        when (words[0].lowercase()) {
            "getaccount" -> {
                val accountId = parseAccountId(words[1])
                val blockId = words.getOrNull(2)?.let { parseBlockIdExt(it) } ?: lastMasterchainBlockId
                getAccount(accountId, blockId)
            }

            "last" -> getLastBlockId()
        }
    }

    private fun parseBlockIdExt(string: String): TonNodeBlockIdExt {
        return if (string.endsWith(')')) {
            TonNodeBlockIdExt(TonNodeBlockId(string))
        } else {
            TonNodeBlockIdExt(string)
        }
    }

    private fun parseAccountId(string: String) = when (string) {
        "none", "root" -> LiteServerAccountId()
        "config" -> TODO()
        "elector" -> TODO()
        "dnsroot" -> TODO()
        else -> LiteServerAccountId(AddrStd(string))
    }

    private fun registerBlockId(blockIdExt: TonNodeBlockIdExt) {
        if (knownBlockIds.contains(blockIdExt)) return
        if (config.blockIdCacheSize > 0 && knownBlockIds.size == config.blockIdCacheSize) {
            knownBlockIds.removeFirst()
        }
        knownBlockIds.addLast(blockIdExt)
    }
}
