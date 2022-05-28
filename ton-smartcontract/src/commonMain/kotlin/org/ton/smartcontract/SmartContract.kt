package org.ton.smartcontract

import org.ton.block.MsgAddressInt
import org.ton.block.StateInit
import org.ton.block.tlb.tlbCodec
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.tlb.storeTlb

interface SmartContract {
    val workchainId: Int
    val name: String
    val code: Cell

    fun createData(): Cell

    fun createStateInit(): StateInit = StateInit(
        code, createData()
    )

    fun address(stateInit: StateInit = createStateInit()): MsgAddressInt.AddrStd =
        address(workchainId, stateInit)

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
