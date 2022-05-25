package org.ton.hashmap.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapEdge
import org.ton.hashmap.HashMapNode
import org.ton.hashmap.HashMapNodeFork
import org.ton.hashmap.HashMapNodeLeaf
import org.ton.tlb.*

fun <X : Any> HashMapNode.Companion.tlbCodec(typeCodec: TlbCodec<X>): TlbCodec<HashMapNode<X>> =
    HashMapNodeTlbCombinator(typeCodec)

private class HashMapNodeTlbCombinator<X : Any>(
    typeCodec: TlbCodec<X>
) : TlbCombinator<HashMapNode<X>>() {
    private val leafConstructor by lazy {
        HashMapNodeLeafTlbConstructor(typeCodec)
    }
    private val forkConstructor by lazy {
        HashMapNodeForkTlbConstructor(typeCodec)
    }

    override val constructors: List<TlbConstructor<out HashMapNode<X>>> by lazy {
        listOf(leafConstructor, forkConstructor)
    }

    override fun getConstructor(value: HashMapNode<X>): TlbConstructor<out HashMapNode<X>> = when (value) {
        is HashMapNodeFork -> forkConstructor
        is HashMapNodeLeaf -> leafConstructor
    }

    override fun decode(cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit): HashMapNode<X> {
        return if (param == 0) {
            leafConstructor.decode(cellSlice, param, negativeParam)
        } else {
            forkConstructor.decode(cellSlice, param, negativeParam)
        }
    }

    private class HashMapNodeLeafTlbConstructor<X : Any>(
        val typeCodec: TlbCodec<X>
    ) : TlbConstructor<HashMapNodeLeaf<X>>(
        schema = "hmn_leaf#_ {X:Type} value:X = HashmapNode 0 X;",
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: HashMapNodeLeaf<X>,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeTlb(value.value, typeCodec)
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): HashMapNodeLeaf<X> = cellSlice {
            val value = loadTlb(typeCodec)
            HashMapNodeLeaf(value)
        }
    }

    private class HashMapNodeForkTlbConstructor<X : Any>(
        typeCodec: TlbCodec<X>
    ) : TlbConstructor<HashMapNodeFork<X>>(
        schema = "hmn_fork#_ {n:#} {X:Type} left:^(Hashmap n X) right:^(Hashmap n X) = HashmapNode (n + 1) X;"
    ) {
        private val hashmapConstructor by lazy {
            HashMapEdge.tlbCodec(typeCodec)
        }

        override fun encode(
            cellBuilder: CellBuilder,
            value: HashMapNodeFork<X>,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            val n = param - 1
            storeRef {
                storeTlb(value.left, hashmapConstructor, n, negativeParam)
            }
            storeRef {
                storeTlb(value.right, hashmapConstructor, n, negativeParam)
            }
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): HashMapNodeFork<X> = cellSlice {
            val n = param - 1
            val left = loadRef {
                loadTlb(hashmapConstructor, n, negativeParam)
            }
            val right = loadRef {
                loadTlb(hashmapConstructor, n, negativeParam)
            }
            HashMapNodeFork(left, right)
        }
    }
}
