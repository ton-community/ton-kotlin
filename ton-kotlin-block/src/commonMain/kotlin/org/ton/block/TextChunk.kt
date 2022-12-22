package org.ton.block

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

class TextChunk(
    val len: UByte,
    val data: BitString,
    val next: TextChunkRef
) : TextChunks {
    companion object {
        fun tlbConstructor(n: Int): TlbConstructor<TextChunk> = TextChunkTlbConstructor(n)
    }
}

private class TextChunkTlbConstructor(
    n: Int
) : TlbConstructor<TextChunk>(
    schema = "text_chunk\$_ {n:#} len:(## 8) data:(bits (len * 8)) next:(TextChunkRef n) = TextChunks (n + 1);"
) {
    val next = TextChunkRef.tlbCombinator(n - 1)

    override fun storeTlb(cellBuilder: CellBuilder, value: TextChunk) {
        cellBuilder.storeUInt8(value.len)
        cellBuilder.storeBits(value.data)
        cellBuilder.storeTlb(next, value.next)
    }

    override fun loadTlb(cellSlice: CellSlice): TextChunk {
        val len = cellSlice.loadUInt8()
        val data = cellSlice.loadBits(len.toInt() * 8)
        val next = cellSlice.loadTlb(next)
        return TextChunk(len, data, next)
    }
}
