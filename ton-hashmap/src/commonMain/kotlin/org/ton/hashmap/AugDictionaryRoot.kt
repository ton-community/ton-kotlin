package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("ahme_root")
@Serializable
data class AugDictionaryRoot<X, Y>(
    val root: AugDictionaryEdge<X, Y>,
    override val extra: Y
) : AugDictionary<X, Y> {
    override fun toString(): String = "ahme_root(root:$root extra:$extra)"

    companion object {
        @JvmStatic
        fun <X, Y> tlbCodec(
            n: Int,
            x: TlbCodec<X>,
            y: TlbCodec<Y>
        ): TlbConstructor<AugDictionaryRoot<X, Y>> = AugDictionaryRootTlbConstructor(n, x, y)
    }
}

private class AugDictionaryRootTlbConstructor<X, Y>(
    val n: Int,
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>
) : TlbConstructor<AugDictionaryRoot<X, Y>>(
    schema = "ahme_root\$1 {n:#} {X:Type} {Y:Type} root:^(AugDictionaryEdge n X Y) extra:Y = AugDictionary n X Y;"
) {
    val edge by lazy {
        AugDictionaryEdge.tlbCodec(n, x, y)
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AugDictionaryRoot<X, Y>
    ) = cellBuilder {
        storeRef {
            storeTlb(edge, value.root)
        }
        storeTlb(y, value.extra)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AugDictionaryRoot<X, Y> = cellSlice {
        val root = loadRef {
            loadTlb(edge)
        }
        val extra = loadTlb(y)
        AugDictionaryRoot(root, extra)
    }
}