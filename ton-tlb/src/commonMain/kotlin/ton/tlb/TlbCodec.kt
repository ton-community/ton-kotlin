package ton.tlb

import ton.cell.CellReader
import ton.cell.CellWriter

interface TlbEncoder<T> {
    fun encode(
        cellWriter: CellWriter,
        value: T,
        typeParam: TlbEncoder<Any>? = null,
        param: Int = 0,
        negativeParam: ((Int) -> Unit)? = null
    )
}

interface TlbDecoder<T> {
    fun decode(
        cellReader: CellReader,
        typeParam: TlbDecoder<Any>? = null,
        param: Int = 0,
        negativeParam: ((Int) -> Unit)? = null
    ): T
}

interface TlbCodec<T> : TlbEncoder<T>, TlbDecoder<T>
