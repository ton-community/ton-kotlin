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

interface SmartContract {
    val liteApi: LiteApi
    val workchainId: Int
    val name: String
    val code: Cell

    fun createDataInit(): Cell

    fun createStateInit(): StateInit = StateInit(
        code, createDataInit()
    )

    fun address(stateInit: StateInit = createStateInit()): AddrStd =
        address(workchainId, stateInit)

    fun createExternalInitMessage(): Message<Cell>

    suspend fun deploy(): LiteServerSendMsgStatus

    override fun toString(): String

    companion object {
        private val stateInitCodec: TlbCodec<StateInit> by lazy {
            StateInit.tlbCodec()
        }

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
