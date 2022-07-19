package org.ton.smartcontract

import org.ton.block.AddrStd
import org.ton.block.Message
import org.ton.block.StateInit
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerSendMsgStatus
import org.ton.tlb.TlbCodec
import org.ton.tlb.storeTlb

/**
 * Basic interface to any smart-contract that can be used to either deploy new contracts to the network or work with
 * already deployed contracts.
 * Only fields that are by definition known for all contracts are present here, more can be freely added by specific implementations
 */
interface SmartContract {
    /**
     * Different workchains may define own mechanisms, therefore it is important to know workchain id.
     *
     * Note: This particular smart-contract interface is only valid for basic workchain (id=0) and masterchain (id=1)
     */
    val workchain_id: Int

    /**
     * Create initial code cell
     */
    fun createCodeInit(): Cell

    /**
     * Create initial data cell
     */
    fun createDataInit(): Cell

    /**
     * Create state_init structure based on [createCodeInit] and [createDataInit]
     */
    fun createStateInit(): StateInit = StateInit(createCodeInit(), createDataInit())

    /**
     * Compute address of a smart-contract based on its [createStateInit]
     */
    fun address(): AddrStd = address(workchain_id, createStateInit())

    companion object {
        private val stateInitCodec: TlbCodec<StateInit> by lazy { StateInit.tlbCodec() }

        /** Compute address of a smart-contract by its [workchainId] and [stateInit] */
        @JvmStatic
        fun address(workchainId: Int, stateInit: StateInit): AddrStd {
            val cell = CellBuilder.createCell {
                storeTlb(stateInitCodec, stateInit)
            }
            val hash = cell.hash()
            return AddrStd(workchainId, hash)
        }
    }
}
