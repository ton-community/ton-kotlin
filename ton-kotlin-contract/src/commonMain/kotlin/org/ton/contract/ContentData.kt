package org.ton.contract

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

sealed interface ContentData {
    data class Snake(val data: org.ton.contract.SnakeData) : org.ton.contract.ContentData

    data class Chunks(val data: org.ton.contract.ChunkedData) : org.ton.contract.ContentData

    companion object :
        TlbCodec<org.ton.contract.ContentData> by _root_ide_package_.org.ton.contract.ContentDataCombinator {
        @JvmStatic
        fun tlbCombinator(): TlbCombinator<org.ton.contract.ContentData> =
            _root_ide_package_.org.ton.contract.ContentDataCombinator
    }
}

private object ContentDataCombinator : TlbCombinator<org.ton.contract.ContentData>() {
    override val constructors: List<TlbConstructor<out org.ton.contract.ContentData>> =
        listOf(
            _root_ide_package_.org.ton.contract.ContentDataCombinator.ContentDataSnakeConstructor,
            _root_ide_package_.org.ton.contract.ContentDataCombinator.ContentDataChunksConstructor
        )

    override fun getConstructor(value: org.ton.contract.ContentData): TlbConstructor<out org.ton.contract.ContentData> =
        when (value) {
            is org.ton.contract.ContentData.Snake -> _root_ide_package_.org.ton.contract.ContentDataCombinator.ContentDataSnakeConstructor
            is _root_ide_package_.org.ton.contract.ContentData.Chunks -> _root_ide_package_.org.ton.contract.ContentDataCombinator.ContentDataChunksConstructor
        }

    private object ContentDataSnakeConstructor : TlbConstructor<_root_ide_package_.org.ton.contract.ContentData.Snake>(
        schema = "snake#00 data:(SnakeData ~n) = ContentData;"
    ) {
        private val snakeDataCodec = _root_ide_package_.org.ton.contract.SnakeData.Companion.tlbCodec()

        override fun storeTlb(cellBuilder: CellBuilder, value: _root_ide_package_.org.ton.contract.ContentData.Snake) {
            cellBuilder.storeTlb(
                _root_ide_package_.org.ton.contract.ContentDataCombinator.ContentDataSnakeConstructor.snakeDataCodec,
                value.data
            )
        }

        override fun loadTlb(cellSlice: CellSlice): _root_ide_package_.org.ton.contract.ContentData.Snake =
            _root_ide_package_.org.ton.contract.ContentData.Snake(cellSlice.loadTlb(_root_ide_package_.org.ton.contract.ContentDataCombinator.ContentDataSnakeConstructor.snakeDataCodec))
    }

    private object ContentDataChunksConstructor :
        TlbConstructor<_root_ide_package_.org.ton.contract.ContentData.Chunks>(
            schema = "chunks#01 data:ChunkedData = ContentData;"
        ) {
        override fun storeTlb(cellBuilder: CellBuilder, value: _root_ide_package_.org.ton.contract.ContentData.Chunks) {
            cellBuilder.storeTlb(_root_ide_package_.org.ton.contract.ChunkedData, value.data)
        }

        override fun loadTlb(cellSlice: CellSlice): _root_ide_package_.org.ton.contract.ContentData.Chunks =
            _root_ide_package_.org.ton.contract.ContentData.Chunks(cellSlice.loadTlb(_root_ide_package_.org.ton.contract.ChunkedData))
    }
}
