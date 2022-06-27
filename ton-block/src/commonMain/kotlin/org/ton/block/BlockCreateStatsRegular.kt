package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("block_create_stats")
data class BlockCreateStatsRegular(
    val counters: HashMapE<CreatorStats>
) : BlockCreateStats {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<BlockCreateStatsRegular> = BlockCreateStatsRegularTlbConstructor
    }
}

private object BlockCreateStatsRegularTlbConstructor : TlbConstructor<BlockCreateStatsRegular>(
    schema = "block_create_stats#17 counters:(HashmapE 256 CreatorStats) = BlockCreateStats;"
) {
    val hashmapE by lazy { HashMapE.tlbCodec(256, CreatorStats.tlbCodec()) }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: BlockCreateStatsRegular
    ) = cellBuilder {
        storeTlb(hashmapE, value.counters)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): BlockCreateStatsRegular = cellSlice {
        val counters = loadTlb(hashmapE)
        BlockCreateStatsRegular(counters)
    }
}