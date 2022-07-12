package org.ton.smartcontract

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

sealed interface ContentData {
    data class Snake(val data: SnakeData) : ContentData

    data class Chunks(val data: ChunkedData) : ContentData

    companion object : TlbCodec<ContentData> by ContentDataCombinator {
        @JvmStatic
        fun tlbCombinator(): TlbCombinator<ContentData> = ContentDataCombinator
    }
}

private object ContentDataCombinator : TlbCombinator<ContentData>() {
    override val constructors: List<TlbConstructor<out ContentData>> =
        listOf(
            ContentDataSnakeConstructor,
            ContentDataChunksConstructor
        )

    override fun getConstructor(value: ContentData): TlbConstructor<out ContentData> = when (value) {
        is ContentData.Snake -> ContentDataSnakeConstructor
        is ContentData.Chunks -> ContentDataChunksConstructor
    }

    private object ContentDataSnakeConstructor : TlbConstructor<ContentData.Snake>(
        schema = "snake#00 data:(SnakeData ~n) = ContentData;"
    ) {
        private val snakeDataCodec = SnakeData.tlbCodec()

        override fun storeTlb(cellBuilder: CellBuilder, value: ContentData.Snake) {
            cellBuilder.storeTlb(snakeDataCodec, value.data)
        }

        override fun loadTlb(cellSlice: CellSlice): ContentData.Snake =
            ContentData.Snake(cellSlice.loadTlb(snakeDataCodec))
    }

    private object ContentDataChunksConstructor : TlbConstructor<ContentData.Chunks>(
        schema = "chunks#01 data:ChunkedData = ContentData;"
    ) {
        override fun storeTlb(cellBuilder: CellBuilder, value: ContentData.Chunks) {
            cellBuilder.storeTlb(ChunkedData, value.data)
        }

        override fun loadTlb(cellSlice: CellSlice): ContentData.Chunks =
            ContentData.Chunks(cellSlice.loadTlb(ChunkedData))
    }
}
