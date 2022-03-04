package ton.tlb.types

import ton.tlb.TlbDecoder

class CellReference<T>(
    val decoderContext: TlbDecoder.() -> TypeExpression<T>,
) : TypeExpression<T> {
    override fun decode(decoder: TlbDecoder): T {
        val cell = decoder.cell.references[decoder.cellRefPointer++]
        val cellDecoder = TlbDecoder(cell, decoder)
        println("Start decoding cell: $cell")
        val result = decoderContext(cellDecoder).decode(cellDecoder)
        println("Stop decoding cell: $cell result: $result")
        return result
    }
}

fun <T> TypeNamedConstructor.cellReference(typeExpression: TlbDecoder.() -> TypeExpression<T>) =
    CellReference(typeExpression)
