package org.ton.contract

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbCombinatorProvider
import org.ton.tlb.storeTlb

sealed interface ContentData {
    data class Snake(val data: SnakeData) : ContentData

    data class Chunks(val data: ChunkedData) : ContentData

    companion object : TlbCombinatorProvider<ContentData> by ContentDataCombinator
}

private object ContentDataCombinator : TlbCombinator<ContentData>() {
    override val constructors: List<TlbConstructor<out ContentData>> =
        listOf(
            ContentDataSnakeConstructor,
            ContentDataChunksConstructor
        )

    override fun getConstructor(value: ContentData): TlbConstructor<out ContentData> =
        when (value) {
            is ContentData.Snake -> ContentDataSnakeConstructor
            is ContentData.Chunks -> ContentDataChunksConstructor
        }

    private object ContentDataSnakeConstructor : TlbConstructor<ContentData.Snake>(
        schema = "snake#00 data:(SnakeData ~n) = ContentData;"
    ) {
        override fun storeTlb(cellBuilder: CellBuilder, value: ContentData.Snake) {
            cellBuilder.storeTlb(
                SnakeData,
                value.data
            )
        }

        override fun loadTlb(cellSlice: CellSlice): ContentData.Snake =
            ContentData.Snake(cellSlice.loadTlb(SnakeData))
    }

    private object ContentDataChunksConstructor :
        TlbConstructor<ContentData.Chunks>(
            schema = "chunks#01 data:ChunkedData = ContentData;"
        ) {
        override fun storeTlb(cellBuilder: CellBuilder, value: ContentData.Chunks) {
            cellBuilder.storeTlb(ChunkedData, value.data)
        }

        override fun loadTlb(cellSlice: CellSlice): ContentData.Chunks =
            ContentData.Chunks(cellSlice.loadTlb(ChunkedData))
    }
}
