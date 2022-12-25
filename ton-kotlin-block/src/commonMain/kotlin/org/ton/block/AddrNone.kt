package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@SerialName("addr_none")
@Serializable
object AddrNone : MsgAddressExt, TlbConstructorProvider<AddrNone> by AddrNoneTlbConstructor {
    override fun toString(): String = "addr_none"
}

private object AddrNoneTlbConstructor : TlbConstructor<AddrNone>(
    schema = "addr_none\$00 = MsgAddressExt;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AddrNone
    ) {
    }

    override fun loadTlb(cellSlice: CellSlice): AddrNone {
        return AddrNone
    }
}
