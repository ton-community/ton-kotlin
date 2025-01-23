package org.ton.block.account

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec

/**
 * Account split depth. Fixed-length 5-bit integer of range 1..=30
 */
public class SplitDepth(
    public val value: Int
) {
    public object Tlb : TlbCodec<SplitDepth> {
        override fun loadTlb(cellSlice: CellSlice): SplitDepth {
            return SplitDepth(cellSlice.loadUInt(BITS).toInt())
        }

        override fun storeTlb(cellBuilder: CellBuilder, value: SplitDepth) {
            cellBuilder.storeUInt(value.value, BITS)
        }
    }

    public companion object {
        /**
         * The number of data bits that this struct occupies.
         */
        public const val BITS: Int = 5
    }
}