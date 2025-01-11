package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
public data class Text(
    val chunks: UByte,
    val rest: TextChunks
) {
    public companion object : TlbConstructorProvider<Text> by TextTlbConstructor
}

private object TextTlbConstructor : TlbConstructor<Text>(
    schema = "text\$_ chunks:(## 8) rest:(TextChunks chunks) = Text;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: Text) {
        cellBuilder.storeUInt8(value.chunks)
        cellBuilder.storeTlb(TextChunks.tlbCodec(value.chunks.toInt()), value.rest)
    }

    override fun loadTlb(cellSlice: CellSlice): Text {
        val chunks = cellSlice.loadUInt(8).toUByte()
        val rest = cellSlice.loadTlb(TextChunks.tlbCodec(chunks.toInt()))
        return Text(chunks, rest)
    }
}
