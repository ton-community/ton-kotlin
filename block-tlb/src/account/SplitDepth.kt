package org.ton.kotlin.account

import org.ton.kotlin.account.SplitDepth.Companion.BITS
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer

/**
 * Account split depth. Fixed-length 5-bit integer of range 1..=30
 */
public class SplitDepth(
    public val value: Int
) {
    public companion object {
        /**
         * The number of data bits that this struct occupies.
         */
        public const val BITS: Int = 5

        public fun cellSerializer(): CellSerializer<SplitDepth> = SplitDepthSerializer
    }
}

private object SplitDepthSerializer : CellSerializer<SplitDepth> {
    override fun load(slice: CellSlice, context: CellContext): SplitDepth {
        return SplitDepth(slice.loadUInt(BITS).toInt())
    }

    override fun store(
        builder: CellBuilder,
        value: SplitDepth,
        context: CellContext
    ) {
        builder.storeInt(value.value, BITS)
    }
}