package org.ton.hashmap

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

public interface HashmapAugNode<X, Y> : TlbObject {
    /**
     * ```tl-b
     * ahmn_leaf#_ {X:Type} {Y:Type} extra:Y value:X = HashmapAugNode 0 X Y;
     */
    public interface AhmnLeaf<X, Y> : HashmapAugNode<X, Y> {
        public val extra: Y
        public val value: X

        override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
            type("ahmn_leaf") {
                field("extra", extra)
                field("value", value)
            }
        }
    }

    /**
     * ```tl-b
     * ahmn_fork#_ {n:#} {X:Type} {Y:Type} left:^(HashmapAug n X Y)
     *   right:^(HashmapAug n X Y) extra:Y = HashmapAugNode (n + 1) X Y;
     */
    public interface AhmnFork<X, Y> : HashmapAugNode<X, Y> {
        public val n: Int
        public val left: CellRef<HashmapAug<X, Y>>
        public val right: CellRef<HashmapAug<X, Y>>
        public val extra: Y

        public fun loadLeft(): HashmapAug<X, Y> = left.value
        public fun loadRight(): HashmapAug<X, Y> = right.value

        override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
            type("ahmn_fork") {
                field("left", left)
                field("right", right)
                field("extra", extra)
            }
        }
    }

    public companion object {
        @JvmStatic
        public fun <X, Y> leaf(extra: Y, value: X): AhmnLeaf<X, Y> =
            AhmnLeafImpl(extra, value)

        @JvmStatic
        public fun <X, Y> fork(
            n: Int,
            left: CellRef<HashmapAug<X, Y>>,
            right: CellRef<HashmapAug<X, Y>>,
            extra: Y
        ): AhmnFork<X, Y> = AhmnForkImpl(n, left, right, extra)
    }
}

private data class AhmnLeafImpl<X, Y>(
    override val extra: Y,
    override val value: X,
) : HashmapAugNode.AhmnLeaf<X, Y> {
    override fun toString(): String = print().toString()
}

private data class AhmnForkImpl<X, Y>(
    override val n: Int,
    override val left: CellRef<HashmapAug<X, Y>>,
    override val right: CellRef<HashmapAug<X, Y>>,
    override val extra: Y,
) : HashmapAugNode.AhmnFork<X, Y> {
    override fun toString(): String = print().toString()
}

private class AhmnLeafTlbConstructor<X, Y>(
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>,
) : TlbConstructor<HashmapAugNode.AhmnLeaf<X, Y>>(
    schema = "ahmn_leaf#_ {X:Type} {Y:Type} extra:Y value:X = HashmapAugNode 0 X Y"
) {
    override fun loadTlb(cellSlice: CellSlice): HashmapAugNode.AhmnLeaf<X, Y> {
        val extra = y.loadTlb(cellSlice)
        val value = x.loadTlb(cellSlice)
        return HashmapAugNode.leaf(extra, value)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: HashmapAugNode.AhmnLeaf<X, Y>) {
        y.storeTlb(cellBuilder, value.extra)
        x.storeTlb(cellBuilder, value.value)
    }
}

private class AhmnForkTlbConstructor<X, Y>(
    val n: Int,
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>,
) : TlbConstructor<HashmapAugNode.AhmnFork<X, Y>>(
    schema = "ahmn_fork#_ {n:#} {X:Type} {Y:Type} left:^(HashmapAug n X Y) right:^(HashmapAug n X Y) extra:Y = HashmapAugNode (n + 1) X Y"
) {
    private val hashmapAug = HashmapAug

    override fun loadTlb(cellSlice: CellSlice): HashmapAugNode.AhmnFork<X, Y> {
        val n = this.n - 1
        val left = cellSlice.loadRef(x)
        val right = cellSlice.loadRef(x)
        val extra = y.loadTlb(cellSlice)
        return HashmapAugNode.fork(n, left, right, extra)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: HashmapAugNode.AhmnFork<X, Y>) {
        cellBuilder.reference(value.left)
        cellBuilder.reference(value.right)
        y.storeTlb(cellBuilder, value.extra)
    }
}
