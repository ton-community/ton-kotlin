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
    public interface AhmnLeaf<X, Y> : HashmapAugNode<X, Y>, AugmentedDictionary.Leaf<X, Y> {
        public override val extra: Y
        public override val value: X

        override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
            type("ahmn_leaf") {
                field("extra", extra)
                field("value", value)
            }
        }

        public companion object {
            @JvmStatic
            public fun <X, Y> tlbCodec(x: TlbCodec<X>, y: TlbCodec<Y>): TlbCodec<AhmnLeaf<X, Y>> =
                AhmnLeafTlbConstructor(x, y)
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

        public companion object {
            @JvmStatic
            public fun <X, Y> tlbCodec(x: TlbCodec<X>, y: TlbCodec<Y>, n: Int): TlbCodec<AhmnFork<X, Y>> =
                AhmnForkTlbConstructor(x, y, n)
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
        ): AhmnFork<X, Y> = AhmnForkImpl(n - 1, left, right, extra)

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        public fun <X, Y> tlbCodec(x: TlbCodec<X>, y: TlbCodec<Y>, n: Int): TlbCodec<HashmapAugNode<X, Y>> {
            return if (n == 0) {
                AhmnLeaf.tlbCodec(x, y)
            } else {
                AhmnFork.tlbCodec(x, y, n)
            } as TlbCodec<HashmapAugNode<X, Y>>
        }
    }
}

private class AhmnLeafImpl<X, Y>(
    override val extra: Y,
    override val value: X,
) : HashmapAugNode.AhmnLeaf<X, Y> {
    override fun toString(): String = print().toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AhmnLeafImpl<*, *>) return false

        if (extra != other.extra) return false
        return value == other.value
    }

    override fun hashCode(): Int {
        var result = extra?.hashCode() ?: 0
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }
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
        return AhmnLeafImpl(extra, value)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: HashmapAugNode.AhmnLeaf<X, Y>) {
        y.storeTlb(cellBuilder, value.extra)
        x.storeTlb(cellBuilder, value.value)
    }
}

private class AhmnForkTlbConstructor<X, Y>(
    x: TlbCodec<X>,
    val y: TlbCodec<Y>,
    n: Int
) : TlbConstructor<HashmapAugNode.AhmnFork<X, Y>>(
    schema = "ahmn_fork#_ {n:#} {X:Type} {Y:Type} left:^(HashmapAug n X Y) right:^(HashmapAug n X Y) extra:Y = HashmapAugNode (n + 1) X Y"
) {
    private val n = n - 1
    private val hashmapAug = HashmapAug.tlbCodec(this.n, x, y)

    override fun loadTlb(cellSlice: CellSlice): HashmapAugNode.AhmnFork<X, Y> {
        val left = cellSlice.loadRef(hashmapAug)
        val right = cellSlice.loadRef(hashmapAug)
        val extra = y.loadTlb(cellSlice)
        return AhmnForkImpl(this.n, left, right, extra)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: HashmapAugNode.AhmnFork<X, Y>) {
        check(value.n == this.n) { "n mismatch, expected: $n, actual: ${value.n}" }
        cellBuilder.storeRef(hashmapAug, value.left)
        cellBuilder.storeRef(hashmapAug, value.right)
        cellBuilder.storeTlb(y, value.extra)
    }
}
