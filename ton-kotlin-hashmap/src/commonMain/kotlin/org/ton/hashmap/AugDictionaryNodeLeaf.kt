package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

@SerialName("ahmn_leaf")
@Serializable
public data class AugDictionaryNodeLeaf<X, Y>(
    override val extra: Y,
    val value: X
) : AugDictionaryNode<X, Y> {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer.type("ahmn_leaf") {
            field("extra", extra)
            field("value", value)
        }
    }

    override fun toString(): String = print().toString()

    public companion object {
        @JvmStatic
        public fun <X, Y> tlbCodec(
            x: TlbCodec<X>,
            y: TlbCodec<Y>
        ): TlbCodec<AugDictionaryNodeLeaf<X, Y>> = AugDictionaryNodeLeafTlbConstructor(x, y)
    }
}

private class AugDictionaryNodeLeafTlbConstructor<X, Y>(
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>
) : TlbConstructor<AugDictionaryNodeLeaf<X, Y>>(
    schema = "ahmn_leaf#_ {X:Type} {Y:Type} extra:Y value:X = AugDictionaryNode 0 X Y;",
    id = BitString.empty()
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
