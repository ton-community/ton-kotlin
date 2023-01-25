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
public sealed interface HashMapE<out T> : Iterable<Pair<BitString, T>>, TlbObject {

    override fun iterator(): Iterator<Pair<BitString, T>> = nodes().iterator()
    public fun nodes(): Sequence<Pair<BitString, T>>
    public fun toMap(): Map<BitString, T> = nodes().toMap()

    public companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        public fun <T> of(): HashMapE<T> = EmptyHashMapE()

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
    EmptyHashMapE::class to EmptyHashMapETlbConstructor,
    RootHashMapE::class to RootHashMapE.tlbConstructor(n, x),
) {
    private object EmptyHashMapETlbConstructor : TlbConstructor<EmptyHashMapE<*>>(
        schema = "hme_empty\$0 {n:#} {X:Type} = HashmapE n X;",
        id = BitString(false)
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: EmptyHashMapE<*>
        ) = Unit

        override fun loadTlb(
            cellSlice: CellSlice
        ): EmptyHashMapE<*> = EmptyHashMapE<Nothing>()
    }
}
