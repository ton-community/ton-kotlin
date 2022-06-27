@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

@Serializable
@JsonClassDiscriminator("@type")
sealed interface HashMapNode<T> {
    companion object {
        @JvmStatic
        fun <X> tlbCodec(n: Int, typeCodec: TlbCodec<X>): TlbCodec<HashMapNode<X>> =
            HashMapNodeTlbCombinator(n, typeCodec)
    }
}

private class HashMapNodeTlbCombinator<X>(
    val n: Int,
    x: TlbCodec<X>
) : TlbCombinator<HashMapNode<X>>() {
    private val leafConstructor by lazy {
        HashMapNodeLeafTlbConstructor(x)
    }
    private val forkConstructor by lazy {
        HashMapNodeForkTlbConstructor(n, x)
    }

    override val constructors: List<TlbConstructor<out HashMapNode<X>>> by lazy {
        listOf(leafConstructor, forkConstructor)
    }

    override fun getConstructor(value: HashMapNode<X>): TlbConstructor<out HashMapNode<X>> = when (value) {
        is HashMapNodeFork -> forkConstructor
        is HashMapNodeLeaf -> leafConstructor
    }

    override fun loadTlb(cellSlice: CellSlice): HashMapNode<X> {
        return if (n == 0) {
            leafConstructor.loadTlb(cellSlice)
        } else {
            forkConstructor.loadTlb(cellSlice)
        }
    }

    private class HashMapNodeLeafTlbConstructor<X>(
        val x: TlbCodec<X>
    ) : TlbConstructor<HashMapNodeLeaf<X>>(
        schema = "hmn_leaf#_ {X:Type} value:X = HashmapNode 0 X;",
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: HashMapNodeLeaf<X>
        ) = cellBuilder {
            storeTlb(x, value.value)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): HashMapNodeLeaf<X> = cellSlice {
            val value = loadTlb(x)
            HashMapNodeLeaf(value)
        }
    }

    private class HashMapNodeForkTlbConstructor<X>(
        n: Int,
        x: TlbCodec<X>
    ) : TlbConstructor<HashMapNodeFork<X>>(
        schema = "hmn_fork#_ {n:#} {X:Type} left:^(Hashmap n X) right:^(Hashmap n X) = HashmapNode (n + 1) X;"
    ) {
        private val hashmapConstructor by lazy {
            HashMapEdge.tlbCodec(n - 1, x)
        }

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: HashMapNodeFork<X>
        ) = cellBuilder {
            storeRef {
                storeTlb(hashmapConstructor, value.left)
            }
            storeRef {
                storeTlb(hashmapConstructor, value.right)
            }
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): HashMapNodeFork<X> = cellSlice {
            val left = loadRef {
                loadTlb(hashmapConstructor)
            }
            val right = loadRef {
                loadTlb(hashmapConstructor)
            }
            HashMapNodeFork(left, right)
        }
    }
}
