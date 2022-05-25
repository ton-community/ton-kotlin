package org.ton.block.tlb

import org.ton.block.MsgAddressExt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

fun MsgAddressExt.Companion.tlbCodec(): TlbCodec<MsgAddressExt> = MsgAddressExtTlbCombinator()

private class MsgAddressExtTlbCombinator : TlbCombinator<MsgAddressExt>() {
    private val addrNoneConstructor by lazy { AddrNoneTlbConstructor() }
    private val addrExternConstructor by lazy { AddrExternTlbConstructor() }

    override val constructors: List<TlbConstructor<out MsgAddressExt>> by lazy {
        listOf(addrNoneConstructor, addrExternConstructor)
    }

    override fun getConstructor(value: MsgAddressExt): TlbConstructor<out MsgAddressExt> = when (value) {
        is MsgAddressExt.AddrNone -> addrNoneConstructor
        is MsgAddressExt.AddrExtern -> addrExternConstructor
    }

    private class AddrNoneTlbConstructor : TlbConstructor<MsgAddressExt.AddrNone>(
        schema = "addr_none\$00 = MsgAddressExt;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: MsgAddressExt.AddrNone
        ) {
        }

        override fun loadTlb(cellSlice: CellSlice): MsgAddressExt.AddrNone {
            return MsgAddressExt.AddrNone
        }
    }

    private class AddrExternTlbConstructor : TlbConstructor<MsgAddressExt.AddrExtern>(
        schema = "addr_extern\$01 len:(## 9) external_address:(bits len) = MsgAddressExt;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: MsgAddressExt.AddrExtern
        ) = cellBuilder {
            storeUInt(value.len, 9)
            storeBits(value.externalAddress)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): MsgAddressExt.AddrExtern = cellSlice {
            val len = loadUInt(9).toInt()
            val externalAddress = loadBitString(len)
            MsgAddressExt.AddrExtern(len, externalAddress)
        }
    }
}
