//package ton.tlb.old.types
//
//import ton.cell.reader
//import ton.tlb.old.TlbDecoder
//
//class CellReference<T>(
//    val decoderContext: TlbDecoder.() -> TypeExpression<T>,
//) : TypeExpression<T> {
//    override fun decode(decoder: TlbDecoder): T {
//        val cell = decoder.reader.readCell()
//        val cellDecoder = TlbDecoder(cell.reader(), decoder)
//        println("Start decoding cell: $cell")
//        val result = decoderContext(cellDecoder).decode(cellDecoder)
//        println("Stop decoding cell: $cell result: $result")
//        return result
//    }
//}
//
//fun <T> TypeNamedConstructor.cellReference(typeExpression: TlbDecoder.() -> TypeExpression<T>) =
//    CellReference(typeExpression)
