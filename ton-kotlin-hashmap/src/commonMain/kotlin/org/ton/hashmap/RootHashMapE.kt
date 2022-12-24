package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("hme_root")
public data class RootHashMapE<T>(
    val rootCellRef: CellRef<HashMapEdge<T>>
) : HashMapE<T> {
    public constructor(root: Cell, tlbCodec: TlbCodec<HashMapEdge<T>>) : this(CellRef(root, tlbCodec))
    public constructor(root: HashMapEdge<T>, tlbCodec: TlbCodec<HashMapEdge<T>>) : this(CellRef(root, tlbCodec))

    val root: HashMapEdge<T> by rootCellRef

    override fun nodes(): Sequence<Pair<BitString, T>> = rootCellRef.value.nodes()

    override fun toString(): String = "(hme_root\nroot:$rootCellRef)"

    public companion object {
        @JvmStatic
        public fun <X> tlbConstructor(n: Int, x: TlbCodec<X>): TlbConstructor<RootHashMapE<X>> =
            RootHashMapETlbConstructor(n, x)
    }
}

private class RootHashMapETlbConstructor<X>(
    n: Int,
    x: TlbCodec<X>
) : TlbConstructor<RootHashMapE<X>>(
    schema = "hme_root\$1 {n:#} {X:Type} root:^(Hashmap n X) = HashmapE n X;",
    id = ID
) {
    private val cellRef = CellRef.tlbCodec(HashMapEdge.tlbCodec(n, x))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: RootHashMapE<X>
    ) {
        cellBuilder.storeTlb(cellRef, value.rootCellRef)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): RootHashMapE<X> {
        val root = cellSlice.loadTlb(cellRef)
        return RootHashMapE(root)
    }

    companion object {
        val ID = BitString(true)
    }
}
