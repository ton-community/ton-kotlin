package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("ahmn_leaf")
@Serializable
data class AugDictionaryNodeLeaf<X, Y>(
    override val extra: Y, val value: X
) : AugDictionaryNode<X, Y> {
    override fun toString(): String = "ahmn_leaf(extra:$extra value:$value)"

    companion object {
        @JvmStatic
        fun <X, Y> tlbCodec(
            x: TlbCodec<X>,
            y: TlbCodec<Y>
        ): TlbConstructor<AugDictionaryNodeLeaf<X, Y>> = AugDictionaryNodeLeafTlbConstructor(x, y)
    }
}

private class AugDictionaryNodeLeafTlbConstructor<X, Y>(
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>
) : TlbConstructor<AugDictionaryNodeLeaf<X, Y>>(
    schema = "ahmn_leaf#_ {X:Type} {Y:Type} extra:Y value:X = AugDictionaryNode 0 X Y;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AugDictionaryNodeLeaf<X, Y>
    ) = cellBuilder {
        storeTlb(y, value.extra)
        storeTlb(x, value.value)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AugDictionaryNodeLeaf<X, Y> = cellSlice {
        val extra = loadTlb(y)
        val value = loadTlb(x)
        AugDictionaryNodeLeaf(extra, value)
    }
}