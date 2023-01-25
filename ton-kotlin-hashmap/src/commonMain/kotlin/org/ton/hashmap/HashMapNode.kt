@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.cell.*
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

@Serializable
@JsonClassDiscriminator("@type")
public sealed interface HashMapNode<out T> : TlbObject {
    public companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        public fun <X> tlbCodec(n: Int, x: TlbCodec<X>): TlbCodec<HashMapNode<X>> =
            if (n == 0) {
                HashMapNodeLeafTlbConstructor(x)
            } else {
                HashMapNodeForkTlbConstructor(n, x)
            } as TlbCodec<HashMapNode<X>>
    }
}

private class HashMapNodeLeafTlbConstructor<X>(
    val x: TlbCodec<X>
) : TlbConstructor<HashMapNodeLeaf<X>>(
    schema = "hmn_leaf#_ {X:Type} value:X = HashmapNode 0 X;",
    id = BitString.empty()
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
    schema = "hmn_fork#_ {n:#} {X:Type} left:^(Hashmap n X) right:^(Hashmap n X) = HashmapNode (n + 1) X;",
    id = BitString.empty()
) {
    private val hashmapConstructor = CellRef.tlbCodec(HashMapEdge.tlbCodec(n - 1, x))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: HashMapNodeFork<X>
    ) = cellBuilder {
        storeTlb(hashmapConstructor, value.leftCellRef)
        storeTlb(hashmapConstructor, value.rightCellRef)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): HashMapNodeFork<X> = cellSlice {
        val left = loadTlb(hashmapConstructor)
        val right = loadTlb(hashmapConstructor)
        HashMapNodeFork(left, right)
    }
}
