package org.ton.tlb

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice

class TlbConstructorBuilder<T : Any>(
    var schema: String = "",
    var id: BitString = BitString(0)
) {
    var negativeParam: () -> Int = { 0 }
    var encoder: TlbEncoder<T> = TlbEncoder { cellBuilder, value, param, negativeParam ->

    }
    var decoder: TlbDecoder<T> = decode { TODO() }

    fun encode(builder: CellBuilder.(T, Int, ((Int) -> Unit)) -> Unit): TlbEncoder<T> {
        val encoder = TlbEncoder(builder)
        this.encoder = encoder
        return encoder
    }

    fun decode(slice: CellSlice.(Int) -> T): TlbDecoder<T> {
        val decoder = object : TlbDecoder<T> {
            override fun decode(
                cellSlice: CellSlice,
                param: Int,
                negativeParam: (Int) -> Unit
            ): T {
                val result = slice(cellSlice, param)
                negativeParam(this@TlbConstructorBuilder.negativeParam())
                return result
            }
        }
        this.decoder = decoder
        return decoder
    }

    fun build() = object : TlbConstructor<T>(schema, id) {
        val encoder = this@TlbConstructorBuilder.encoder
        val decoder = this@TlbConstructorBuilder.decoder

        override fun encode(
            cellBuilder: CellBuilder,
            value: T,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = encoder.encode(cellBuilder, value, param, negativeParam)

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): T = decoder.decode(cellSlice, param, negativeParam)
    }
}

fun <T : Any> buildTlbConstructor(
    schema: String,
    id: BitString = BitString(0),
    builder: TlbConstructorBuilder<T>.() -> Unit
) = TlbConstructorBuilder<T>(schema, id).apply(builder).build()
