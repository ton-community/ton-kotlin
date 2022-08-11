package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.hashmap.AugDictionary
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.UIntTlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("block_create_stats_ext")
data class BlockCreateStatsExt(
    val counters: AugDictionary<CreatorStats, Long>
) : BlockCreateStats {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<BlockCreateStatsExt> = BlockCreateStateExtTlbConstructor
    }
}

private object BlockCreateStateExtTlbConstructor : TlbConstructor<BlockCreateStatsExt>(
    schema = "block_create_stats_ext#34 counters:(HashmapAugE 256 CreatorStats uint32) = BlockCreateStats;\n"
) {
    val counters = AugDictionary.tlbCodec(256, CreatorStats.tlbCodec(), UIntTlbConstructor.long(32))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: BlockCreateStatsExt
    ) = cellBuilder {
        storeTlb(counters, value.counters)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): BlockCreateStatsExt = cellSlice {
        val counters = loadTlb(counters)
        BlockCreateStatsExt(counters)
    }
}
