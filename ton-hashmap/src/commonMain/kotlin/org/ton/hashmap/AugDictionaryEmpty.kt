package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("ahme_empty")
@Serializable
data class AugDictionaryEmpty<X, Y>(
    override val extra: Y
) : AugDictionary<X, Y> {
    override fun toString(): String = "ahme_empty(extra:$extra)"

    companion object {
        @JvmStatic
        fun <X, Y> tlbCodec(
            y: TlbCodec<Y>
        ): TlbConstructor<AugDictionaryEmpty<X, Y>> = AugDictionaryEmptyTlbConstructor(y)
    }
}

private class AugDictionaryEmptyTlbConstructor<X, Y>(
    val y: TlbCodec<Y>,
) : TlbConstructor<AugDictionaryEmpty<X, Y>>(
    schema = "ahme_empty\$0 {n:#} {X:Type} {Y:Type} extra:Y = AugDictionary n X Y;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AugDictionaryEmpty<X, Y>
    ) = cellBuilder {
        storeTlb(y, value.extra)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AugDictionaryEmpty<X, Y> = cellSlice {
        val extra = loadTlb(y)
        AugDictionaryEmpty(extra)
    }
}