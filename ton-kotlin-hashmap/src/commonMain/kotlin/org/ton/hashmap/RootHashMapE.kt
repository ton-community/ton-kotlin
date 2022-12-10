package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.loadRef
import org.ton.cell.storeRef
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("hme_root")
data class RootHashMapE<out T>(
    val root: HashMapEdge<T>
) : HashMapE<T> {

    override fun nodes(): Sequence<Pair<BitString, T>> = root.nodes()

    override fun toString(): String = "(hme_root\nroot:$root)"

    companion object {
        @JvmStatic
        fun <X> tlbConstructor(n: Int, x: TlbCodec<X>): TlbConstructor<RootHashMapE<X>> =
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
