package org.ton.block.shard

import org.ton.cell.*
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

public data class ShardStateSplit(
    val left: ShardStateUnsplit,
    val right: ShardStateUnsplit
) : ShardState {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("split_state") {
        field("left", left)
        field("right", right)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<ShardStateSplit> by SplitStateTlbConstructor
}

private object SplitStateTlbConstructor : TlbConstructor<ShardStateSplit>(
    schema = "split_state#5f327da5 left:^ShardStateUnsplit right:^ShardStateUnsplit = ShardState;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ShardStateSplit
    ) = cellBuilder {
        storeRef {
            storeTlb(ShardStateUnsplit, value.left)
        }
        storeRef {
            storeTlb(ShardStateUnsplit, value.right)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ShardStateSplit = cellSlice {
        val left = loadRef {
            loadTlb(ShardStateUnsplit)
        }
        val right = loadRef {
            loadTlb(ShardStateUnsplit)
        }
        ShardStateSplit(left, right)
    }
}
