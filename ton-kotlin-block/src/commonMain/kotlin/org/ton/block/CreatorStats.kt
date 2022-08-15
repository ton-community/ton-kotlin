package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("creator_info")
data class CreatorStats(
    val mc_blocks: Counters,
    val shard_blocks: Counters
) {
    companion object : TlbConstructorProvider<CreatorStats> by CreatorStatsTlbConstructor
}

private object CreatorStatsTlbConstructor : TlbConstructor<CreatorStats>(
    schema = "creator_info#4 mc_blocks:Counters shard_blocks:Counters = CreatorStats;\n"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: CreatorStats
    ) = cellBuilder {
        storeTlb(Counters, value.mc_blocks)
        storeTlb(Counters, value.shard_blocks)
    }

    override fun loadTlb(cellSlice: CellSlice): CreatorStats = cellSlice {
        val mcBlocks = loadTlb(Counters)
        val shardBlocks = loadTlb(Counters)
        CreatorStats(mcBlocks, shardBlocks)
    }
}
