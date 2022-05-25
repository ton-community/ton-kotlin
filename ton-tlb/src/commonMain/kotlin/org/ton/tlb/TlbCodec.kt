package org.ton.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice

fun interface TlbEncoder<T> {
    fun encode(cellBuilder: CellBuilder, value: T, param: Int) =
        encode(cellBuilder, value, param) {}

    fun encode(cellBuilder: CellBuilder, value: T, negativeParam: ((Int) -> Unit)) =
        encode(cellBuilder, value, 0, negativeParam)

    fun encode(
        cellBuilder: CellBuilder,
        value: T,
        param: Int,
        negativeParam: ((Int) -> Unit)
    )
}

fun <T> TlbEncoder(encoder: CellBuilder.(T, Int, ((Int) -> Unit)) -> Unit) =
    TlbEncoder<T> { cellBuilder, value, param, negativeParam ->
        encoder(cellBuilder, value, param, negativeParam)
    }

interface TlbDecoder<T> {
    fun decode(cellSlice: CellSlice, param: Int): T =
        decode(cellSlice, param)

    fun decode(cellSlice: CellSlice, negativeParam: ((Int) -> Unit)): T =
        decode(cellSlice, 0, negativeParam)

    fun decode(
        cellSlice: CellSlice,
        param: Int = 0,
        negativeParam: ((Int) -> Unit) = {}
    ): T
}

abstract class TlbCodec<T> : TlbEncoder<T>, TlbDecoder<T> {
    abstract override fun toString(): String
}

fun <T : Any> CellSlice.loadTlb(codec: TlbDecoder<T>, param: Int = 0, negativeParam: ((Int) -> Unit) = {}): T {
    println("start deserialize: $codec")
    val result = codec.decode(this, param, negativeParam)
    println("complete serialize: $codec === $result")
    return result
}

fun <T : Any> CellBuilder.storeTlb(
    value: T, codec: TlbEncoder<T>, param: Int = 0, negativeParam: (Int) -> Unit = {}
) = apply {
    codec.encode(this, value, param, negativeParam)
}
