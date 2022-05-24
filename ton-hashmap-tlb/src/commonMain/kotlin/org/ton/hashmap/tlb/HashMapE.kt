package org.ton.hashmap.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.EmptyHashMapE
import org.ton.hashmap.HashMapE
import org.ton.hashmap.HashMapEdge
import org.ton.hashmap.RootHashMapE
import org.ton.tlb.*

fun <X : Any> HashMapE.Companion.tlbCodec(typeCodec: TlbCodec<X>): TlbCodec<HashMapE<X>> =
    HashMapETlbCombinator(typeCodec)

private class HashMapETlbCombinator<T : Any>(
    typeCodec: TlbCodec<T>
) : TlbCombinator<HashMapE<T>>() {
    private val rootConstructor by lazy {
        RootHashMapETlbConstructor(typeCodec)
    }
    private val emptyConstructor by lazy {
        EmptyHashMapETlbConstructor<T>()
    }

    override val constructors by lazy {
        listOf(rootConstructor, emptyConstructor)
    }

    override fun getConstructor(value: HashMapE<T>): TlbConstructor<out HashMapE<T>> = when (value) {
        is RootHashMapE -> rootConstructor
        is EmptyHashMapE -> emptyConstructor
    }

    class EmptyHashMapETlbConstructor<X : Any> : TlbConstructor<EmptyHashMapE<X>>(
        schema = "hme_empty\$0 {n:#} {X:Type} = HashmapE n X;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: EmptyHashMapE<X>,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = Unit

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): EmptyHashMapE<X> = EmptyHashMapE()
    }

    class RootHashMapETlbConstructor<X : Any>(
        typeCodec: TlbCodec<X>
    ) : TlbConstructor<RootHashMapE<X>>(
        schema = "hme_root\$1 {n:#} {X:Type} root:^(Hashmap n X) = HashmapE n X;"
    ) {
        private val hashmapConstructor by lazy {
            HashMapEdge.tlbCodec(typeCodec)
        }

        override fun encode(
            cellBuilder: CellBuilder,
            value: RootHashMapE<X>,
            param: Int,
            negativeParam: (Int) -> Unit
        ) {
            cellBuilder.storeRef {
                storeTlb(value.root, hashmapConstructor, param)
            }
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): RootHashMapE<X> {
            val root = cellSlice.loadRef {
                cellSlice.loadTlb(hashmapConstructor, param)
            }
            return RootHashMapE(root)
        }
    }
}
