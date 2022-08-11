package org.ton.contract

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapE
import org.ton.tlb.*
import org.ton.tlb.constructor.tlbCodec

data class ChunkedData(
    val data: HashMapE<org.ton.contract.SnakeDataTail>
) {
    companion object : TlbCodec<org.ton.contract.ChunkedData> by org.ton.contract.ChunkedDataCombinator {
        @JvmStatic
        fun tlbCombinator(): TlbCombinator<org.ton.contract.ChunkedData> = org.ton.contract.ChunkedDataCombinator
    }
}

private object ChunkedDataCombinator : TlbCombinator<org.ton.contract.ChunkedData>() {
    override val constructors: List<TlbConstructor<out org.ton.contract.ChunkedData>> =
        listOf(org.ton.contract.ChunkedDataCombinator.ChunkedDataConstructor)

    override fun getConstructor(value: org.ton.contract.ChunkedData): TlbConstructor<out org.ton.contract.ChunkedData> =
        org.ton.contract.ChunkedDataCombinator.ChunkedDataConstructor

    private object ChunkedDataConstructor : TlbConstructor<org.ton.contract.ChunkedData>(
        schema = "chunked_data#_ data:(HashMapE 32 ^(SnakeData ~0)) = ChunkedData;"
    ) {
        // SnakeData ~0  is SnakeDataTail
        private val dataCodec =
            HashMapE.tlbCodec(32, Cell.tlbCodec(org.ton.contract.SnakeDataTail.Companion.tlbCodec()))

        override fun storeTlb(cellBuilder: CellBuilder, value: org.ton.contract.ChunkedData) {
            cellBuilder.storeTlb(org.ton.contract.ChunkedDataCombinator.ChunkedDataConstructor.dataCodec, value.data)
        }

        override fun loadTlb(cellSlice: CellSlice): org.ton.contract.ChunkedData =
            org.ton.contract.ChunkedData(cellSlice.loadTlb(org.ton.contract.ChunkedDataCombinator.ChunkedDataConstructor.dataCodec))
    }
}
