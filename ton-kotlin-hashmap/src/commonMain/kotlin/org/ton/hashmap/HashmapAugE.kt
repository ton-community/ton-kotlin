@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

@JsonClassDiscriminator("@type")
public interface HashmapAugE<X, Y> : AugmentedDictionary<X, Y>, TlbObject {

    /**
     * ```tl-b
     * ahme_empty$0 {n:#} {X:Type} {Y:Type} extra:Y = HashmapAugE n X Y;
     */
    public interface AhmeEmpty<X, Y> : HashmapAugE<X, Y> {
        public val n: Int

        public val extra: Y

        override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
            type("ahme_empty") {
                field("extra", extra)
            }
        }

        public companion object {
            public fun <X, Y> tlbCodec(n: Int, y: TlbCodec<Y>): TlbCodec<AhmeEmpty<X, Y>> =
                AhmeEmptyTlbConstructor(n, y)
        }
    }

    /**
     * ```tl-b
     * ahme_root$1 {n:#} {X:Type} {Y:Type} root:^(HashmapAug n X Y) extra:Y = HashmapAugE n X Y;
     */
    public interface AhmeRoot<X, Y> : HashmapAugE<X, Y> {
        public val n: Int

        public val root: CellRef<HashmapAug<X, Y>>
        public val extra: Y

        public fun loadRoot(): HashmapAug<X, Y> = root.value

        override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
            type("ame_root") {
                field("root", root)
                field("extra", extra)
            }
        }

        public companion object {
            public fun <X, Y> tlbCodec(n: Int, x: TlbCodec<X>, y: TlbCodec<Y>): TlbCodec<AhmeRoot<X, Y>> =
                AhmeRootTlbConstructor(n, x, y)
        }
    }

    public companion object {
        @JvmStatic
        public fun <X, Y> empty(n: Int, extra: Y): AhmeEmpty<X, Y> =
            AhmeEmptyImpl(n, extra)

        @JvmStatic
        public fun <X, Y> root(n : Int, root: CellRef<HashmapAug<X, Y>>, extra: Y): AhmeRoot<X, Y> =
            AhmeRootImpl(n, root, extra)

        @Suppress("UNCHECKED_CAST")
        public fun <X, Y> tlbCodec(n: Int, x: TlbCodec<X>, y: TlbCodec<Y>): TlbCodec<HashmapAugE<X, Y>> =
            HashmapAugETlbCombinator(n, x, y) as TlbCodec<HashmapAugE<X, Y>>
    }
}

private data class AhmeEmptyImpl<X, Y>(
    override val n: Int,
    override val extra: Y,
) : HashmapAugE.AhmeEmpty<X, Y> {
    override fun get(key: BitString): Pair<X?, Y> = null to extra

    override fun iterator(): Iterator<AugmentedDictionary.Entry<X, Y>> = EmptyAugDictionaryIterator()

    override fun toString(): String = print().toString()
}

private class EmptyAugDictionaryIterator<X, Y> : Iterator<AugmentedDictionary.Entry<X, Y>> {
    override fun hasNext(): Boolean = false
    override fun next(): AugmentedDictionary.Entry<X, Y> = throw NoSuchElementException()
}

private data class AhmeRootImpl<X, Y>(
    override val n: Int,
    override val root: CellRef<HashmapAug<X, Y>>,
    override val extra: Y,
) : HashmapAugE.AhmeRoot<X, Y> {
    override fun get(key: BitString): Pair<X?, Y> {
        val edge = root.value as HashmapAug.AhmEdge<X, Y>
        return edge[key]
    }

    override fun iterator(): Iterator<AugmentedDictionary.Entry<X, Y>> {
        val edge = root.value as HashmapAug.AhmEdge<X, Y>
        return edge.iterator()
    }

    override fun toString(): String = print().toString()
}

private class HashmapAugETlbCombinator<X,Y>(
    val n: Int,
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>,
    val ahmeEmptyCodec: TlbCodec<HashmapAugE.AhmeEmpty<X, Y>> = HashmapAugE.AhmeEmpty.tlbCodec(n, y),
    val ahmeRootCodec: TlbCodec<HashmapAugE.AhmeRoot<X, Y>> = HashmapAugE.AhmeRoot.tlbCodec(n, x, y)
) : TlbCombinator<HashmapAugE<*, *>>(
    HashmapAugE::class,
    HashmapAugE.AhmeEmpty::class to ahmeEmptyCodec,
    HashmapAugE.AhmeRoot::class to ahmeRootCodec,
)

private class AhmeEmptyTlbConstructor<X, Y>(
    private val n: Int,
    private val y: TlbCodec<Y>,
) : TlbConstructor<HashmapAugE.AhmeEmpty<X, Y>>(
    schema = "ahme_empty\$0 {n:#} {X:Type} {Y:Type} extra:Y = HashmapAugE n X Y"
) {
    override fun loadTlb(cellSlice: CellSlice): HashmapAugE.AhmeEmpty<X, Y> {
        val extra = y.loadTlb(cellSlice)
        return AhmeEmptyImpl(n, extra)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: HashmapAugE.AhmeEmpty<X, Y>) {
        require(value.n == n) { "n mismatch, expected: $n, actual: ${value.n}" }
        y.storeTlb(cellBuilder, value.extra)
    }
}

private class AhmeRootTlbConstructor<X, Y>(
    private val n: Int,
    x: TlbCodec<X>,
    private val y: TlbCodec<Y>,
) : TlbConstructor<HashmapAugE.AhmeRoot<X, Y>>(
    schema = "ahme_root\$1 {n:#} {X:Type} {Y:Type} root:^(HashmapAug n X Y) extra:Y = HashmapAugE n X Y"
) {
    private val hashmapAug = HashmapAug.tlbCodec(n, x, y)

    override fun loadTlb(cellSlice: CellSlice): HashmapAugE.AhmeRoot<X, Y> {
        val root = cellSlice.loadRef(hashmapAug)
        val extra = y.loadTlb(cellSlice)
        return AhmeRootImpl(n, root, extra)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: HashmapAugE.AhmeRoot<X, Y>) {
        require(value.n == n) { "n mismatch, expected: $n, actual: ${value.n}" }
        cellBuilder.storeRef(hashmapAug, value.root)
        y.storeTlb(cellBuilder, value.extra)
    }
}
