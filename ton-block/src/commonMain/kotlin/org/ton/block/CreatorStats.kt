package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("creator_info")
data class CreatorStats(
    val mc_blocks: Counters,
    val shard_blocks: Counters
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<CreatorStats> = CreatorStatsTlbConstructor
    }
}

private object CreatorStatsTlbConstructor : TlbConstructor<CreatorStats>(
    schema = "creator_info#4 mc_blocks:Counters shard_blocks:Counters = CreatorStats;\n"
) {
    val counters by lazy { Counters.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: CreatorStats
    ) = cellBuilder {
        storeTlb(counters, value.mc_blocks)
        storeTlb(counters, value.shard_blocks)
    }

    override fun loadTlb(cellSlice: CellSlice): CreatorStats = cellSlice {
        val mcBlocks = loadTlb(counters)
        val shardBlocks = loadTlb(counters)
        CreatorStats(mcBlocks, shardBlocks)
    }
}