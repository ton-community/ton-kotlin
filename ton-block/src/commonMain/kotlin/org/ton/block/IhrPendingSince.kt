package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("ihr_pending")
data class IhrPendingSince(
    val import_lt: Long
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<IhrPendingSince> = IhrPendingSinceTlbConstructor
    }
}

private object IhrPendingSinceTlbConstructor : TlbConstructor<IhrPendingSince>(
    schema = "ihr_pending\$_ import_lt:uint64 = IhrPendingSince;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: IhrPendingSince
    ) = cellBuilder {
        storeUInt(value.import_lt, 64)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): IhrPendingSince = cellSlice {
        val importLt = loadUInt(64).toLong()
        IhrPendingSince(importLt)
    }
}