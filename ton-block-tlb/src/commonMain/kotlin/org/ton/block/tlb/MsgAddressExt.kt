package org.ton.block.tlb

import org.ton.block.MsgAddressExt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

fun MsgAddressExt.Companion.tlbCodec(): TlbCodec<MsgAddressExt> = MsgAddressExtTlbCombinator

private object MsgAddressExtTlbCombinator : TlbCombinator<MsgAddressExt>(
    AddrNoneTlbConstructor, AddrExternTlbConstructor
) {
    override fun getConstructor(value: MsgAddressExt): TlbConstructor<out MsgAddressExt> = when (value) {
        is MsgAddressExt.AddrExtern -> AddrExternTlbConstructor
        is MsgAddressExt.AddrNone -> AddrNoneTlbConstructor
    }

    private object AddrNoneTlbConstructor : TlbConstructor<MsgAddressExt.AddrNone>(
        schema = "addr_none\$00 = MsgAddressExt;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: MsgAddressExt.AddrNone,
            param: Int,
            negativeParam: (Int) -> Unit
        ) {
        }

        override fun decode(cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit): MsgAddressExt.AddrNone {
            return MsgAddressExt.AddrNone
        }
    }

    private object AddrExternTlbConstructor : TlbConstructor<MsgAddressExt.AddrExtern>(
        schema = "addr_extern\$01 len:(## 9) external_address:(bits len) = MsgAddressExt;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: MsgAddressExt.AddrExtern,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeUInt(value.len, 9)
            storeBits(value.externalAddress)
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): MsgAddressExt.AddrExtern = cellSlice {
            val len = loadUInt(9).toInt()
            val externalAddress = loadBitString(len)
            MsgAddressExt.AddrExtern(len, externalAddress)
        }
    }
}
