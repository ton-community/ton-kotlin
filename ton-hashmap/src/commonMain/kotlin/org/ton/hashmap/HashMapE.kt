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
sealed interface HashMapE<T> {
    fun <K> toMap(keyTransform: (BitString) -> K): Map<K, T>
    fun toMap(): Map<BitString, T>

    companion object {
        @JvmStatic
        fun <T> of(): HashMapE<T> = EmptyHashMapE()

        @JvmStatic
        fun <X> tlbCodec(n: Int, x: TlbCodec<X>): TlbCodec<HashMapE<X>> =
            HashMapETlbCombinator(n, x)
    }
}

private class HashMapETlbCombinator<X>(
    n: Int,
    x: TlbCodec<X>
) : TlbCombinator<HashMapE<X>>() {
    private val rootConstructor = RootHashMapETlbConstructor(n, x)

    @Suppress("UNCHECKED_CAST")
    private val emptyConstructor = EmptyHashMapETlbConstructor as TlbConstructor<EmptyHashMapE<X>>

    override val constructors =
        listOf(rootConstructor, emptyConstructor)

    override fun getConstructor(value: HashMapE<X>): TlbConstructor<out HashMapE<X>> = when (value) {
        is RootHashMapE -> rootConstructor
        is EmptyHashMapE -> emptyConstructor
        else -> throw UnknownTlbConstructorException()
    }

    private object EmptyHashMapETlbConstructor : TlbConstructor<EmptyHashMapE<Any>>(
        schema = "hme_empty\$0 {n:#} {X:Type} = HashmapE n X;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: EmptyHashMapE<Any>
        ) = Unit

        override fun loadTlb(
            cellSlice: CellSlice
        ): EmptyHashMapE<Any> = EmptyHashMapE()
    }

    private class RootHashMapETlbConstructor<X>(
        n: Int,
        x: TlbCodec<X>
    ) : TlbConstructor<RootHashMapE<X>>(
        schema = "hme_root\$1 {n:#} {X:Type} root:^(Hashmap n X) = HashmapE n X;",
        id = ID
    ) {
        private val hashmapConstructor = HashMapEdge.tlbCodec(n, x)

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: RootHashMapE<X>
        ) {
            cellBuilder.storeRef {
                storeTlb(hashmapConstructor, value.root)
            }
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): RootHashMapE<X> {
            val root = cellSlice.loadRef {
                loadTlb(hashmapConstructor)
            }
            return RootHashMapE(root)
        }

        companion object {
            val ID = BitString(true)
        }
    }
}

