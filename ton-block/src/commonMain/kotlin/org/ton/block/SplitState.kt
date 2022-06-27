package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("split_state")
data class SplitState(
    val left: ShardStateUnsplit,
    val right: ShardStateUnsplit
) : ShardState {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<SplitState> = SplitStateTlbConstructor
    }
}

private object SplitStateTlbConstructor : TlbConstructor<SplitState>(
    schema = "split_state#5f327da5 left:^ShardStateUnsplit right:^ShardStateUnsplit = ShardState;"
) {
    val shardStateUnsplit by lazy { ShardStateUnsplit.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: SplitState
    ) = cellBuilder {
        storeRef {
            storeTlb(shardStateUnsplit, value.left)
        }
        storeRef {
            storeTlb(shardStateUnsplit, value.right)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): SplitState = cellSlice {
        val left = loadRef {
            loadTlb(shardStateUnsplit)
        }
        val right = loadRef {
            loadTlb(shardStateUnsplit)
        }
        SplitState(left, right)
    }
}
