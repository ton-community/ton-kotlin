@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellSlice
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

public sealed interface HashMapE<T> : Iterable<Pair<BitString, T>>, TlbObject {

    override fun iterator(): Iterator<Pair<BitString, T>>

    public fun set(key: BitString, value: T): HmeRoot<T>

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
            var i = -1
            map.forEach { (key, value) ->
                require(!key.isEmpty()) { "Empty key" }
                if (i == -1) i = key.size
                else require(i == key.size) { "Variable length key, expected: $i, actual: (${key.size}) $key" }
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
    x: TlbCodec<X>,
    root: TlbConstructor<HmeRoot<X>> = HmeRoot.tlbConstructor(n, x)
) : TlbCombinator<HashMapE<*>>(
    HashMapE::class,
    HmeEmpty::class to EmptyHashMapETlbConstructor,
    HmeRoot::class to root,
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
