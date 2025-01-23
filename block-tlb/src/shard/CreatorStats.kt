package org.ton.block.shard

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

/**
 * Block production statistics for the single validator.
 */
public data class CreatorStats(
    /**
     * Masterchain block production statistics.
     */
    val mcBlocks: BlockCounters,

    /**
     * Block production statistics for other workchains.
     */
    val shardBlocks: BlockCounters
) {
    public object Tlb : TlbCodec<CreatorStats> {
        private const val TAG = 0x4

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: CreatorStats
        ): Unit = cellBuilder {
            storeUInt(TAG, 4)
            storeTlb(BlockCounters.Tlb, value.mcBlocks)
            storeTlb(BlockCounters.Tlb, value.shardBlocks)
        }

        override fun loadTlb(cellSlice: CellSlice): CreatorStats = cellSlice {
            check(TAG == loadUInt(4).toInt()) {
                "Invalid TAG"
            }
            val mcBlocks = loadTlb(BlockCounters.Tlb)
            val shardBlocks = loadTlb(BlockCounters.Tlb)
            CreatorStats(mcBlocks, shardBlocks)
        }
    }
}