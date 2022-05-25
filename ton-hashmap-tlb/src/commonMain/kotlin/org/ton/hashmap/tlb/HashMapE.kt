package org.ton.hashmap.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.EmptyHashMapE
import org.ton.hashmap.HashMapE
import org.ton.hashmap.HashMapEdge
import org.ton.hashmap.RootHashMapE
import org.ton.tlb.*

fun <X : Any> HashMapE.Companion.tlbCodec(n: Int, x: TlbCodec<X>): TlbCodec<HashMapE<X>> =
    HashMapETlbCombinator(n, x)

private class HashMapETlbCombinator<X : Any>(
    n: Int,
    x: TlbCodec<X>
) : TlbCombinator<HashMapE<X>>() {
    private val rootConstructor by lazy {
        RootHashMapETlbConstructor(n, x)
    }
    private val emptyConstructor by lazy {
        EmptyHashMapETlbConstructor<X>()
    }

    override val constructors by lazy {
        listOf(rootConstructor, emptyConstructor)
    }

    override fun getConstructor(value: HashMapE<X>): TlbConstructor<out HashMapE<X>> = when (value) {
        is RootHashMapE -> rootConstructor
        is EmptyHashMapE -> emptyConstructor
    }

    private class EmptyHashMapETlbConstructor<X : Any> : TlbConstructor<EmptyHashMapE<X>>(
        schema = "hme_empty\$0 {n:#} {X:Type} = HashmapE n X;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: EmptyHashMapE<X>
        ) = Unit

        override fun loadTlb(
            cellSlice: CellSlice
        ): EmptyHashMapE<X> = EmptyHashMapE()
    }

    private class RootHashMapETlbConstructor<X : Any>(
        n: Int,
        x: TlbCodec<X>
    ) : TlbConstructor<RootHashMapE<X>>(
        schema = "hme_root\$1 {n:#} {X:Type} root:^(Hashmap n X) = HashmapE n X;"
    ) {
        private val hashmapConstructor by lazy {
            HashMapEdge.tlbCodec(n, x)
        }

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
                cellSlice.loadTlb(hashmapConstructor)
            }
            return RootHashMapE(root)
        }
    }
}
