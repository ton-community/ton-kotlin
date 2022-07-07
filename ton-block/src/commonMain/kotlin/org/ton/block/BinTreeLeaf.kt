package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("bt_leaf")
data class BinTreeLeaf<X>(
    val leaf: X
) : BinTree<X> {

    override fun nodes(): Sequence<X> = sequenceOf(leaf)

    companion object {
        @JvmStatic
        fun <X> tlbCodec(
            x: TlbCodec<X>
        ): TlbConstructor<BinTreeLeaf<X>> = BinTreeTlbConstructor(x)
    }
}

private class BinTreeTlbConstructor<X>(
    val x: TlbCodec<X>
) : TlbConstructor<BinTreeLeaf<X>>(
    schema = "bt_leaf\$0 {X:Type} leaf:X = BinTree X;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: BinTreeLeaf<X>
    ) = cellBuilder {
        storeTlb(x, value.leaf)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): BinTreeLeaf<X> = cellSlice {
        val leaf = loadTlb(x)
        BinTreeLeaf(leaf)
    }
}