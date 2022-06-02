package org.ton.smartcontract

import org.ton.block.Message
import org.ton.block.MsgAddressInt
import org.ton.block.StateInit
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerSendMsgStatus
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

    fun address(stateInit: StateInit = createStateInit()): MsgAddressInt.AddrStd =
        address(workchainId, stateInit)

    fun createExternalInitMessage(): Message<Cell>

    suspend fun deploy(): LiteServerSendMsgStatus

    override fun toString(): String

    companion object {
        private val stateInitCodec by lazy {
            StateInit.tlbCodec()
        }

        @JvmStatic
        fun address(workchainId: Int, stateInit: StateInit): MsgAddressInt.AddrStd {
            val cell = CellBuilder.createCell {
                storeTlb(stateInitCodec, stateInit)
            }
            val hash = cell.hash()
            return MsgAddressInt.AddrStd(workchainId, hash)
        }
    }
}
