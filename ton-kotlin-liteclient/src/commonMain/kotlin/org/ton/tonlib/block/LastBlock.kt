package org.ton.tonlib.block

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerBlockLinkBack
import org.ton.lite.api.liteserver.LiteServerBlockLinkForward
import org.ton.lite.api.liteserver.LiteServerPartialBlockProof
import org.ton.lite.api.liteserver.functions.LiteServerGetBlockProof
import org.ton.lite.api.liteserver.functions.LiteServerGetMasterchainInfo
import org.ton.tonlib.LastBlockState
import org.ton.tonlib.ZeroStateIdExt
import kotlin.coroutines.CoroutineContext
import kotlin.math.max
import kotlin.math.min

internal class LastBlock(
    override val coroutineContext: CoroutineContext,
    val liteApi: LiteApi,
    state: LastBlockState,
    val config: LiteClientConfigGlobal
) : CoroutineScope {
    private val minSeqno = run {
        var result = 0
        if (state.lastKeyBlockId.isValid()) {
            result = state.lastKeyBlockId.seqno
        }
        val configInitBlock = BlockIdExt(config.validator.initBlock)
        if (configInitBlock.isValid() && configInitBlock != state.initBlockId) {
            result = min(result, configInitBlock.seqno)
        }
        result
    }
    private val maxSeqno = atomic(0)
    private val currentSeqno = atomic(minSeqno)
    private val _state = MutableStateFlow(
        state.copy(
            lastBlockId = state.lastKeyBlockId
        )
    )
    val state get() = _state.asStateFlow()
    val task = Channel<CompletableDeferred<Unit>>()
    private val _syncState = MutableStateFlow(
        SyncState(0, 0, 0)
    )

    init {
        println("[Last Block] State: ${_state.value}")
    }

    suspend fun sync(): LastBlockState {
        updateSync()

        updateZeroState(_state.value.zeroStateId)
        updateZeroState(ZeroStateIdExt(config.validator.zeroState))

        if (task.isEmpty) {
            withContext(coroutineContext) {
                launch {
                    getMasterchainInfo()
                    checkInitBlock()
                    getLastBlock()

                    while (true) {
                        val task = task.receiveCatching().getOrNull() ?: break
                        task.complete(Unit)
                    }
                }
            }
        }

        val promise = CompletableDeferred<Unit>()
        task.send(promise)
        promise.await()

        return _state.value
    }

    private suspend fun getMasterchainInfo() {
        println("get_masterchain_info: start")
        val info = liteApi(LiteServerGetMasterchainInfo)
        updateZeroState(ZeroStateIdExt(info.init))
        maxSeqno.value = max(maxSeqno.value, info.last.seqno)
        println("get_masterchain_info: done")
    }

    private suspend fun checkInitBlock() {
        val configInitBlock = BlockIdExt(config.validator.initBlock)
        val currentState = _state.value
        if (!configInitBlock.isValid()) {
            println("checkInitBlock: skip - no init_block in config")
            return
        } else if (configInitBlock == currentState.initBlockId) {
            println("checkInitBlock: skip - init_block already checked")
            return
        } else {
            if (currentState.lastKeyBlockId.seqno >= configInitBlock.seqno) {
                println("checkInitBlock: start - init_block -> last_block")
                doCheckInitBlock(configInitBlock, currentState.lastKeyBlockId)
            } else {
                println("checkInitBlock: start - last_block -> init_block")
                doCheckInitBlock(currentState.lastKeyBlockId, configInitBlock)
            }
        }
    }

    private suspend fun doCheckInitBlock(
        from: BlockIdExt,
        to: BlockIdExt,
    ) {
        println("checkInitBlock: continue - $from -> $to")
        val blockProof = liteApi(
            LiteServerGetBlockProof(
                mode = 1,
                knownBlock = from.toTl(),
                targetBlock = to.toTl()
            )
        )
        processBlockProof(from, blockProof)
    }

    private suspend fun processBlockProof(
        from: BlockIdExt,
        blockProof: LiteServerPartialBlockProof,
    ): BlockProofChain {
        println("Got proof \n  FROM: ${blockProof.from}\n    TO: ${blockProof.to}")
        val chain = deserializeProofChain(blockProof)
        require(chain.from == from) {
            "block proof chain starts from block mismatch,\nexpected: $from\n  actual: ${chain.from}"
        }
        chain.validate()
        return chain
    }

    private suspend fun getLastBlock() {
        println("getLastBlock: start")
        var chain: BlockProofChain
        while (true) {
            val currentState = _state.value
            val from = currentState.lastBlockId
            println("getLastBlock: continue $from -> ?")
            val blockProof = liteApi(LiteServerGetBlockProof(0, from.toTl(), null))
            chain = processBlockProof(from, blockProof)
            updateState(chain)
            if (chain.isComplete) {
                println("get_last_block: done")
                break
            }
        }
    }

    private fun updateZeroState(zeroStateId: ZeroStateIdExt) {
        if (!zeroStateId.isValid()) {
            println("Ignore invalid zero state update")
            return
        }
        val currentState = _state.value
        if (!currentState.zeroStateId.isValid()) {
            println("Init zero state: $zeroStateId")
            _state.update {
                it.copy(
                    zeroStateId = zeroStateId
                )
            }
            return
        }
        if (currentState.zeroStateId == zeroStateId) {
            return
        }
        throw IllegalStateException("Masterchain zero state mismatch, expected: ${currentState.zeroStateId}, actual: $zeroStateId")
    }

    private fun updateState(chain: BlockProofChain) {
        val state = _state.value
        currentSeqno.value = max(currentSeqno.value, chain.to.seqno)
        maxSeqno.value = max(maxSeqno.value, currentSeqno.value)
        val isChanged = state.isNeedUpdateLastBlockId(chain.to) || state.isNeedUpdateLastKeyBlock(chain.keyBlockId)
        if (isChanged) {
            _state.update {
                state.copy(
                    lastBlockId = chain.to,
                    lastKeyBlockId = chain.keyBlockId ?: state.lastKeyBlockId,
                )
            }
        }
    }

    private fun LastBlockState.isNeedUpdateLastBlockId(mcBlockId: BlockIdExt): Boolean =
        mcBlockId.isValid() && (!lastBlockId.isValid() || lastBlockId.id.seqno < mcBlockId.id.seqno)

    private fun LastBlockState.isNeedUpdateLastKeyBlock(mcKeyBlockId: BlockIdExt?): Boolean =
        mcKeyBlockId != null && mcKeyBlockId.isValid() && (!lastKeyBlockId.isValid() || lastKeyBlockId.id.seqno < mcKeyBlockId.id.seqno)

    private fun LastBlockState.isNeedUpdateUtime(utime: Long): Boolean =
        utime > this.utime

    private fun getSyncState(): SyncState {
        return SyncState(
            minSeqno,
            maxSeqno.value,
            currentSeqno.value
        )
    }

    private fun updateSync() {
        val newState = getSyncState()
        if (newState == _syncState.value) {
            return
        }
        _syncState.value = newState
        println("Sync state: $newState")
    }

    private fun deserializeProofChain(tl: LiteServerPartialBlockProof): BlockProofChain {
        var hasKeyBlock = false
        var keyBlockId: BlockIdExt? = null
        val links = tl.steps.map { step ->
            when (step) {
                is LiteServerBlockLinkBack -> {
                    BlockProofLink(
                        from = BlockIdExt(step.from),
                        to = BlockIdExt(step.to),
                        isKey = step.toKeyBlock,
                        destProof = Cell(step.destProof),
                        proof = Cell(step.proof),
                        stateProof = Cell(step.stateProof)
                    )
                }

                is LiteServerBlockLinkForward -> {
                    BlockProofLink(
                        from = BlockIdExt(step.from),
                        to = BlockIdExt(step.to),
                        isKey = step.toKeyBlock,
                        ccSeqno = step.signatures.catchainSeqno,
                        validatorSetHash = step.signatures.validatorSetHash,
                        signatures = step.signatures.map(::BlockSignature),
                        destProof = Cell(step.destProof),
                        proof = Cell(step.configProof),
                    )
                }
            }.also { link ->
                if (link.isKey && (!hasKeyBlock || (keyBlockId?.seqno ?: 0) < link.to.seqno)) {
                    hasKeyBlock = true
                    keyBlockId = link.to
                }
            }
        }
        return BlockProofChain(
            from = BlockIdExt(tl.from),
            to = BlockIdExt(tl.to),
            isComplete = tl.complete,
            links = links,
            keyBlockId = keyBlockId
        )
    }

    data class SyncState(
        val minSeqno: Int,
        val maxSeqno: Int,
        val currentSeqno: Int
    ) {
        val progress: Int
            get() = (currentSeqno - minSeqno) * 100 / (maxSeqno - minSeqno)

        override fun toString(): String = "${currentSeqno - minSeqno} / ${maxSeqno - minSeqno} ($progress%)"
    }
}

private fun Cell(byteArray: ByteArray): Cell {
    return BagOfCells(byteArray).first()
}
