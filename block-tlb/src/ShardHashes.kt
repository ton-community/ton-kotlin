package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapE
import org.ton.tlb.CellRef
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbObject
import org.ton.tlb.TlbPrettyPrinter
import kotlin.jvm.JvmInline


@JvmInline
public value class ShardHashes(
    public val value: HashMapE<CellRef<BinTree<ShardDescr>>>
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return value.print(printer)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbCodec<ShardHashes> by ShardHashesTlbCodec
}

private object ShardHashesTlbCodec : TlbCodec<ShardHashes> {
    private val codec = HashMapE.tlbCodec(32, CellRef.tlbCodec(BinTree.tlbCodec(ShardDescr)))
    override fun storeTlb(cellBuilder: CellBuilder, value: ShardHashes) {
        codec.storeTlb(cellBuilder, value.value)
    }

    override fun loadTlb(cellSlice: CellSlice): ShardHashes {
        return ShardHashes(codec.loadTlb(cellSlice))
    }
}
