@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.loadRef
import org.ton.cell.storeRef
import org.ton.tlb.*
import org.ton.tlb.exception.UnknownTlbConstructorException

@Serializable
@JsonClassDiscriminator("@type")
sealed interface HashMapE<T> : Iterable<Pair<BitString, T>> {

    override fun iterator(): Iterator<Pair<BitString, T>> = nodes().iterator()
    fun nodes(): Sequence<Pair<BitString, T>>
    fun toMap(): Map<BitString, T> = nodes().toMap()

    companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T> of(): HashMapE<T> = EmptyHashMapE as HashMapE<T>

        @JvmStatic
        fun <X> tlbCodec(n: Int, x: TlbCodec<X>): TlbCodec<HashMapE<X>> =
            HashMapETlbCombinator(n, x)
    }
}

private class HashMapETlbCombinator<X>(
    n: Int,
    x: TlbCodec<X>
) : TlbCombinator<HashMapE<X>>() {
    private val rootConstructor = RootHashMapE.tlbConstructor(n, x)

    @Suppress("UNCHECKED_CAST")
    private val emptyConstructor = EmptyHashMapETlbConstructor as TlbConstructor<HashMapE<X>>

    override val constructors =
        listOf(rootConstructor, emptyConstructor)

    override fun getConstructor(value: HashMapE<X>): TlbConstructor<out HashMapE<X>> = when (value) {
        is RootHashMapE -> rootConstructor
        is EmptyHashMapE -> emptyConstructor
        else -> throw UnknownTlbConstructorException()
    }

    private object EmptyHashMapETlbConstructor : TlbConstructor<EmptyHashMapE>(
        schema = "hme_empty\$0 {n:#} {X:Type} = HashmapE n X;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: EmptyHashMapE
        ) = Unit

        override fun loadTlb(
            cellSlice: CellSlice
        ): EmptyHashMapE = EmptyHashMapE
    }
}
