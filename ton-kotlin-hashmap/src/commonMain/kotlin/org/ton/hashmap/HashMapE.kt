@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

@Serializable
@JsonClassDiscriminator("@type")
public sealed interface HashMapE<T> : Iterable<Pair<BitString, T>>, TlbObject {

    override fun iterator(): Iterator<Pair<BitString, T>> = nodes().iterator()
    public fun nodes(): Sequence<Pair<BitString, T>>
    public fun toMap(): Map<BitString, T> = nodes().toMap()

    public fun set(key: BitString, value: T): HmeRoot<T>
    public operator fun get(key: BitString): T? = nodes().find { it.first == key }?.second

    /**
     * Checks whether a dictionary is empty.
     */
    public fun isEmpty(): Boolean

    public companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        public fun <T> of(): HashMapE<T> = HmeEmpty()

        @JvmStatic
        public fun <T> empty(): HashMapE<T> = HmeEmpty()

        @JvmStatic
        public fun <T> root(root: CellRef<HmEdge<T>>): HashMapE<T> = HmeRoot(root)

        public fun <T> fromMap(map: Map<BitString, T>?): HashMapE<T> {
            var hashMap = empty<T>()
            if (map == null) return hashMap
            map.forEach { (key, value) ->
                hashMap = hashMap.set(key, value)
            }
            return hashMap
        }

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        public fun <X : Any> tlbCodec(n: Int, x: TlbCodec<X>): TlbCodec<HashMapE<X>> {
            return HashMapETlbCombinator(n, x) as TlbCodec<HashMapE<X>>
        }
    }
}

private class HashMapETlbCombinator<X>(
    n: Int,
    x: TlbCodec<X>
) : TlbCombinator<HashMapE<*>>(
    HashMapE::class,
    HmeEmpty::class to EmptyHashMapETlbConstructor,
    HmeRoot::class to HmeRoot.tlbConstructor(n, x),
) {
    private object EmptyHashMapETlbConstructor : TlbConstructor<HmeEmpty<*>>(
        schema = "hme_empty\$0 {n:#} {X:Type} = HashmapE n X;",
        id = BitString(false)
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: HmeEmpty<*>
        ) = Unit

        override fun loadTlb(
            cellSlice: CellSlice
        ): HmeEmpty<*> = HmeEmpty<Nothing>()
    }
}
