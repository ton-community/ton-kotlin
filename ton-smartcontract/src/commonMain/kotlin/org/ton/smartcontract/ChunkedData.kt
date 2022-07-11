package org.ton.smartcontract

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

data class ChunkedData(
    val data: HashMapE<SnakeDataTail>
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<ChunkedData> = ChunkedDataCombinator()
    }
}

private class ChunkedDataCombinator : TlbCombinator<ChunkedData>() {
    private val chunkedDataConstructor by lazy { ChunkedDataConstructor() }

    override val constructors: List<TlbConstructor<out ChunkedData>> by lazy { listOf(chunkedDataConstructor) }

    override fun getConstructor(value: ChunkedData): TlbConstructor<out ChunkedData> = chunkedDataConstructor

    private class ChunkedDataConstructor : TlbConstructor<ChunkedData>(
        schema = "chunked_data#_ data:(HashMapE 32 ^(SnakeData ~0)) = ChunkedData;"
    ) {
        private val snakeDataTailCodec by lazy { SnakeDataTail.tlbCodec() }
        private val snakeDataTailNegatedCodec by lazy { Cell.tlbCodec(snakeDataTailCodec) }
        private val hashMapECodec by lazy { HashMapE.tlbCodec(32, snakeDataTailNegatedCodec) }

        override fun storeTlb(cellBuilder: CellBuilder, value: ChunkedData) {
            cellBuilder.storeTlb(hashMapECodec, value.data)
        }

        override fun loadTlb(cellSlice: CellSlice): ChunkedData = ChunkedData(cellSlice.loadTlb(hashMapECodec))
    }
}
