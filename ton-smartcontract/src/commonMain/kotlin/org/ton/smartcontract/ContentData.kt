package org.ton.smartcontract

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

sealed interface ContentData {
    data class Snake(val data: SnakeData) : ContentData

    data class Chunks(val data: ChunkedData) : ContentData

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<ContentData> = ContentDataCombinator()
    }
}

private class ContentDataCombinator : TlbCombinator<ContentData>() {
    private val snakeConstructor by lazy { ContentDataSnakeConstructor() }
    private val chunksConstructor by lazy { ContentDataChunksConstructor() }

    override val constructors: List<TlbConstructor<out ContentData>> by lazy { listOf(snakeConstructor, chunksConstructor)}

    override fun getConstructor(value: ContentData): TlbConstructor<out ContentData> = when(value) {
        is ContentData.Snake -> snakeConstructor
        is ContentData.Chunks -> chunksConstructor
    }

    private class ContentDataSnakeConstructor : TlbConstructor<ContentData.Snake>(
        schema = "snake#00 data:(SnakeData ~n) = ContentData;"
    ) {
        private val snakeDataCodec by lazy { SnakeData.tlbCodec() }

        override fun storeTlb(cellBuilder: CellBuilder, value: ContentData.Snake) {
            cellBuilder.storeTlb(snakeDataCodec, value.data)
        }

        override fun loadTlb(cellSlice: CellSlice): ContentData.Snake = ContentData.Snake(cellSlice.loadTlb(snakeDataCodec))
    }

    private class ContentDataChunksConstructor : TlbConstructor<ContentData.Chunks>(
        schema = "chunks#01 data:ChunkedData = ContentData;"
    ) {
        private val chunkedDataCodec by lazy { ChunkedData.tlbCodec()}

        override fun storeTlb(cellBuilder: CellBuilder, value: ContentData.Chunks) {
            cellBuilder.storeTlb(chunkedDataCodec, value.data)
        }

        override fun loadTlb(cellSlice: CellSlice): ContentData.Chunks = ContentData.Chunks(cellSlice.loadTlb(chunkedDataCodec))
    }
}
