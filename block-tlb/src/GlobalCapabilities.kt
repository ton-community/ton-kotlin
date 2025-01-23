package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec

/**
 * A set of enabled capabilities.
 */
public data class GlobalCapabilities(
    val value: Long
) {
    public object Tlb : TlbCodec<GlobalCapabilities> {
        override fun storeTlb(cellBuilder: CellBuilder, value: GlobalCapabilities) {
            cellBuilder.storeUInt(value.value, 64)
        }

        override fun loadTlb(cellSlice: CellSlice): GlobalCapabilities {
            val value = cellSlice.loadULong(64).toLong()
            return GlobalCapabilities(value)
        }
    }
}