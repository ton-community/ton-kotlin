package org.ton.smartcontract

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapE
import org.ton.tlb.*
import org.ton.tlb.constructor.tlbCodec

data class ChunkedData(
    val data: HashMapE<SnakeDataTail>
) {
    companion object : TlbCodec<ChunkedData> by ChunkedDataCombinator {
        @JvmStatic
        fun tlbCombinator(): TlbCombinator<ChunkedData> = ChunkedDataCombinator
    }
}

private object ChunkedDataCombinator : TlbCombinator<ChunkedData>() {
    override val constructors: List<TlbConstructor<out ChunkedData>> = listOf(ChunkedDataConstructor)

    override fun getConstructor(value: ChunkedData): TlbConstructor<out ChunkedData> = ChunkedDataConstructor

    private object ChunkedDataConstructor : TlbConstructor<ChunkedData>(
        schema = "chunked_data#_ data:(HashMapE 32 ^(SnakeData ~0)) = ChunkedData;"
    ) {
        // SnakeData ~0  is SnakeDataTail
        private val dataCodec = HashMapE.tlbCodec(32, Cell.tlbCodec(SnakeDataTail.tlbCodec()))

        override fun storeTlb(cellBuilder: CellBuilder, value: ChunkedData) {
            cellBuilder.storeTlb(dataCodec, value.data)
        }

        override fun loadTlb(cellSlice: CellSlice): ChunkedData = ChunkedData(cellSlice.loadTlb(dataCodec))
    }
}
