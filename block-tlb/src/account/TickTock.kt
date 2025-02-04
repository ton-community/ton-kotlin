package org.ton.block.account

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec

/**
 * Special transactions execution flags.
 */
public data class TickTock(
    /**
     * Account will be called at the beginning of each block.
     */
    val tick: Boolean,

    /**
     * Account will be called at the end of each block.
     */
    val tock: Boolean
) {
    public object Tlb : TlbCodec<TickTock> {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: TickTock
        ): Unit = cellBuilder {
            storeBoolean(value.tick)
            storeBoolean(value.tock)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): TickTock = cellSlice {
            val tick = loadBit()
            val tock = loadBit()
            TickTock(tick, tock)
        }
    }

    public companion object {
        public const val BITS: Int = 2
    }
}

