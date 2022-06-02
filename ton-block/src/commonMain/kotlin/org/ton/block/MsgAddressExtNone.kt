package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@SerialName("addr_none")
@Serializable
object MsgAddressExtNone : MsgAddressExt {
    override fun toString(): String = "addr_none"

    @JvmStatic
    fun tlbCodec(): TlbConstructor<MsgAddressExtNone> = AddrNoneTlbConstructor
}

private object AddrNoneTlbConstructor : TlbConstructor<MsgAddressExtNone>(
    schema = "addr_none\$00 = MsgAddressExt;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgAddressExtNone
    ) {
    }

    override fun loadTlb(cellSlice: CellSlice): MsgAddressExtNone {
        return MsgAddressExtNone
    }
}
