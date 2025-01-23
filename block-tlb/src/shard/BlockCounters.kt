package org.ton.block.shard

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec

/**
 * Block counters with absolute value and rates.
 */
public data class BlockCounters(
    /**
     * Unix timestamp in seconds of the last counters update.
     */
    val lastUpdated: Long,

    /**
     * Total counter value.
     */
    val total: Long,

    /**
     * Scaled counter rate.
     */
    val cnt2048: Long,

    /**
     * Scaled counter rate (better precision).
     */
    val cnt65536: Long
) {
    public object Tlb : TlbCodec<BlockCounters> {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: BlockCounters
        ): Unit = cellBuilder {
            storeUInt32(value.lastUpdated.toUInt())
            storeUInt64(value.total.toULong())
            storeUInt64(value.cnt2048.toULong())
            storeUInt64(value.cnt65536.toULong())
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): BlockCounters = cellSlice {
            val lastUpdated = loadUInt().toLong()
            val total = loadULong().toLong()
            val cnt2048 = loadULong().toLong()
            val cnt65535 = loadULong().toLong()
            BlockCounters(lastUpdated, total, cnt2048, cnt65535)
        }
    }
}

