package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("bt_fork")
data class BinTreeFork<X>(
    val left: BinTree<X>,
    val right: BinTree<X>
) : BinTree<X> {

    override fun nodes(): Sequence<X> = left.nodes() + right.nodes()

    override fun toString(): String = buildString {
        append("(bt_fork\n")
        append("left:^")
        append(left)
        append(" right:^")
        append(right)
        append(")")
    }

    companion object {
        @JvmStatic
        fun <X> tlbCodec(
            x: TlbCodec<X>
        ): TlbConstructor<BinTreeFork<X>> = BinTreeForkTlbConstructor(x)
    }
}

private class BinTreeForkTlbConstructor<X>(
    val x: TlbCodec<X>
) : TlbConstructor<BinTreeFork<X>>(
    schema = "bt_fork\$1 {X:Type} left:^(BinTree X) right:^(BinTree X) = BinTree X;"
) {
    private val binTree by lazy { BinTree.tlbCodec(x) }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: BinTreeFork<X>
    ) = cellBuilder {
        storeRef { storeTlb(binTree, value.left) }
        storeRef { storeTlb(binTree, value.right) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): BinTreeFork<X> = cellSlice {
        val left = loadRef { loadTlb(binTree) }
        val right = loadRef { loadTlb(binTree) }
        BinTreeFork(left, right)
    }
}