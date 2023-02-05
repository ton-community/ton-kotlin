package org.ton.tonlib

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.api.liteserver.LiteServerDesc
import org.ton.lite.api.LiteApiClient
import org.ton.lite.client.LiteClient
import org.ton.tonlib.block.BlockIdExt
import org.ton.tonlib.block.LastBlock
import org.ton.tonlib.functions.Init
import org.ton.tonlib.functions.TonLibApi
import org.ton.tonlib.types.*
import kotlin.coroutines.CoroutineContext

internal class TonlibClient(
    override val coroutineContext: CoroutineContext = Dispatchers.Default,
) : TonLibApi, CoroutineScope {
    lateinit var kv: KeyValue
    var rawLastBlock: LastBlock? = null
    lateinit var fullConfig: FullConfig
    lateinit var liteClient: LiteApiClient

    internal data class FullConfig(
        val config: LiteClientConfigGlobal,
        val lastState: LastBlockState,
        val lastStateKey: String,
        val walletId: Int,
        val rWalletInitPublicKey: String
    )

    override suspend operator fun invoke(query: Init): OptionsInfo {
        val options = query.options
        kv = when (options.keystoreType) {
            KeyStoreType.IN_MEMORY -> KeyValueInMemory()
            else -> TODO()
        }
        val config = options.config
        val fullConfig = validateConfig(config)
        setConfig(fullConfig)
        val configInfo = fullConfig.toTonlibApi()
        return OptionsInfo(configInfo)
    }

    override suspend operator fun invoke(sync: Sync): TonBlockIdExt {
        val lastBlock = requireNotNull(rawLastBlock).sync()
        return lastBlock.lastBlockId.toTonlibApi()
    }

    private fun validateConfig(config: Config): FullConfig {
        require(config.config.isNotEmpty()) { "No configs provided" }
        val newConfig = MasterConfig.parse(config.config)
        val lastStateKey: String
        val masterConfig: LiteClientConfigGlobal?
        if (config.blockchainName.isEmpty()) {
            lastStateKey = newConfig.validator.zeroState.rootHash.hex()
            masterConfig = MasterConfig[newConfig.validator.zeroState.rootHash]
        } else {
            lastStateKey = config.blockchainName
            masterConfig = MasterConfig[config.blockchainName] ?: MasterConfig[newConfig.validator.zeroState.rootHash]
        }
        if (masterConfig != null) {
            require(masterConfig.validator.zeroState.rootHash == newConfig.validator.zeroState.rootHash) {
                "zero_state mismatch"
            }
            require(masterConfig.validator.hardforks == newConfig.validator.hardforks) {
                "hardforks mismatch"
            }
        }
        val vertSeqno = newConfig.validator.hardforks.size
        val zeroState = ZeroStateIdExt(newConfig.validator.zeroState)

        val cachedState: LastBlockState? = null
        val lastBlockId: BlockIdExt
        var lastKeyBlockId: BlockIdExt
        if (config.ignoreCache || cachedState == null) {
            lastBlockId = BlockIdExt(newConfig.validator.zeroState)
            lastKeyBlockId = lastBlockId
        } else {
            require(cachedState.zeroStateId == zeroState) {
                "zero_state mismatch"
            }
            check(cachedState.vertSeqno <= vertSeqno) {
                "vert_seqno in cached state is bigger"
            }
            lastBlockId = cachedState.lastBlockId
            lastKeyBlockId = cachedState.lastKeyBlockId
        }

        val newConfigInitBlockId = BlockIdExt(newConfig.validator.initBlock)
        var userDefinedInitBlock = false
        if (newConfigInitBlockId.isValid() && lastKeyBlockId.id.seqno < newConfigInitBlockId.id.seqno) {
            lastKeyBlockId = newConfigInitBlockId
            userDefinedInitBlock = true
            println("Using init block from USER config: $newConfigInitBlockId")
        }

        if (masterConfig != null && !userDefinedInitBlock) {
            val masterConfigInitBlockId = BlockIdExt(masterConfig.validator.initBlock)
            if (masterConfigInitBlockId.isValid() && lastKeyBlockId.id.seqno < masterConfigInitBlockId.id.seqno) {
                lastKeyBlockId = masterConfigInitBlockId
                println("Using init block from MASTER config: $masterConfigInitBlockId")
            }
        }

        return FullConfig(
            config = newConfig,
            lastState = LastBlockState(
                lastBlockId = lastBlockId,
                lastKeyBlockId = lastKeyBlockId,
                utime = 0,
                initBlockId = BlockIdExt(newConfig.validator.initBlock),
                zeroStateId = zeroState,
                vertSeqno = vertSeqno,
            ),
            lastStateKey = lastStateKey,
            walletId = ByteReadPacket(newConfig.validator.zeroState.rootHash.toByteArray()).readIntLittleEndian(),
            rWalletInitPublicKey = "Puasxr0QfFZZnYISRphVse7XHKfW7pZU5SJarVHXvQ+rpzkD"
        )
    }

    private fun setConfig(fullConfig: FullConfig) {
        this.fullConfig = fullConfig
        initLiteClient(fullConfig.config.liteServers)
        initLastBlock(fullConfig.lastState)
    }

    private fun initLastBlock(state: LastBlockState) {
        // TODO: save to lastBlockStorage
        rawLastBlock?.cancel()
        rawLastBlock = LastBlock(coroutineContext, liteClient, state, fullConfig.config).apply {
            launch {
                this@apply.state.collectLatest(::updateLastBlockState)
            }
        }
    }

    private fun initLiteClient(liteServers: Collection<LiteServerDesc>) {
        require(liteServers.isNotEmpty()) { "No lite servers provided" }
        liteClient = LiteClient(coroutineContext, liteServers).liteApi
    }

    private fun updateLastBlockState(state: LastBlockState) {
        // TODO: save to lastBlockStorage
    }
}

private fun TonlibClient.FullConfig.toTonlibApi(): OptionsConfigInfo =
    OptionsConfigInfo(walletId.toLong(), rWalletInitPublicKey)

private fun BlockIdExt.toTonlibApi(): TonBlockIdExt =
    TonBlockIdExt(
        workchain = workchain,
        shard = shard,
        seqno = seqno,
        rootHash = rootHash.toByteArray(),
        fileHash = fileHash.toByteArray()
    )
