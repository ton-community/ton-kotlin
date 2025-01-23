package org.ton.block.shard

import org.ton.block.BinTree
import org.ton.block.ShardDescr
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapE
import org.ton.tlb.CellRef
import org.ton.tlb.TlbCodec

/**
 * A tree of the most recent descriptions for all currently existing shards for all workchains except the masterchain.
 */
public class ShardHashes(
    public val value: HashMapE<CellRef<BinTree<ShardDescr>>>
) {
    public object Tlb : TlbCodec<ShardHashes> {
        private val codec = HashMapE.Companion.tlbCodec(
            32, CellRef.Companion.tlbCodec(
                BinTree.Companion.tlbCodec(
                    ShardDescr.Companion
                )
            )
        )

        override fun storeTlb(cellBuilder: CellBuilder, value: ShardHashes) {
            codec.storeTlb(cellBuilder, value.value)
        }

        override fun loadTlb(cellSlice: CellSlice): ShardHashes {
            return ShardHashes(codec.loadTlb(cellSlice))
        }
    }
}