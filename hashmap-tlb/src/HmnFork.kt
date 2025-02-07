package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellSlice
import org.ton.tlb.*

@Serializable
@SerialName("hmn_fork")
public data class HmnFork<T>(
    val left: CellRef<HmEdge<T>>,
    val right: CellRef<HmEdge<T>>
) : HashMapNode<T> {
    public constructor(
        left: HmEdge<T>,
        right: HmEdge<T>,
    ) : this(
        left = CellRef(left),
        right = CellRef(right)
    )

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("hmn_fork") {
        field("left", left)
        field("right", right)
    }

    public fun set(key: BitString, value: T): HmnFork<T> {
        return if (key[0]) {
            copy(
                left = left,
                right = CellRef(right.value.set(key.substring(1), value))
            )
        } else {
            copy(
                left = CellRef(left.value.set(key.substring(1), value)),
                right = right,
            )
        }
    }

    override fun toString(): String = print().toString()

    public companion object {
        public fun <X> tlbCodec(n: Int, x: TlbCodec<X>): TlbCodec<HmnFork<X>> =
            HashMapNodeForkTlbConstructor(n, x)
    }
}

private class HashMapNodeForkTlbConstructor<X>(
    n: Int,
    x: TlbCodec<X>
) : TlbConstructor<HmnFork<X>>(
    schema = "hmn_fork#_ {n:#} {X:Type} left:^(Hashmap n X) right:^(Hashmap n X) = HashmapNode (n + 1) X;",
    id = BitString.empty()
) {
    private val hashmapConstructor = CellRef.tlbCodec(HmEdge.tlbCodec(n - 1, x))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: HmnFork<X>
    ) = cellBuilder {
        storeTlb(hashmapConstructor, value.left)
        storeTlb(hashmapConstructor, value.right)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): HmnFork<X> = cellSlice {
        val left = loadTlb(hashmapConstructor)
        val right = loadTlb(hashmapConstructor)
        HmnFork(left, right)
    }
}
