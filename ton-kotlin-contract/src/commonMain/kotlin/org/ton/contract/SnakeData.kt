package org.ton.contract

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.loadRef
import org.ton.cell.storeRef
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbCombinatorProvider
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

sealed interface SnakeData {
    companion object : TlbCombinatorProvider<SnakeData> by SnakeDataTlbCombinator

    private object SnakeDataTlbCombinator : TlbCombinator<SnakeData>() {
        override val constructors = listOf(SnakeDataCons.tlbConstructor(), SnakeDataTail.tlbConstructor())

        override fun getConstructor(value: SnakeData): TlbConstructor<out SnakeData> = when (value) {
            is SnakeDataTail -> SnakeDataTail.tlbConstructor()
            is SnakeDataCons -> SnakeDataCons.tlbConstructor()
        }

        override fun loadTlb(cellSlice: CellSlice): SnakeData {
            return if (cellSlice.refs.lastIndex >= cellSlice.refsPosition) {
                cellSlice.loadTlb(SnakeDataCons) // More references available, this is a cons
            } else {
                cellSlice.loadTlb(SnakeDataTail) // No more refs, this has to be a tail
            }
        }
    }
}

data class SnakeDataTail(
    val bits: BitString
) : SnakeData {
    companion object : TlbConstructorProvider<SnakeDataTail> by SnakeDataTailTlbConstructor

    private object SnakeDataTailTlbConstructor : TlbConstructor<SnakeDataTail>(
        schema = "tail#_ {bn:#} b:(bits bn) = SnakeData ~0;"
    ) {
        override fun storeTlb(cellBuilder: CellBuilder, value: SnakeDataTail) {
            cellBuilder.storeBits(value.bits)
        }

        override fun loadTlb(cellSlice: CellSlice): SnakeDataTail =
            SnakeDataTail(cellSlice.loadBits(cellSlice.bits.size - cellSlice.bitsPosition))
    }
}

data class SnakeDataCons(
    val bits: BitString,
    val next: SnakeData
) : SnakeData {
    companion object : TlbConstructorProvider<SnakeDataCons> by SnakeDataConsTlbConstructor

    private object SnakeDataConsTlbConstructor : TlbConstructor<SnakeDataCons>(
        schema = "cons#_ {bn:#} {n:#} b:(bits bn) next:^(SnakeData ~n) = SnakeData (n + 1);"
    ) {
        override fun storeTlb(cellBuilder: CellBuilder, value: SnakeDataCons) {
            cellBuilder.storeBits(value.bits)
            cellBuilder.storeRef {
                storeTlb(SnakeData, value.next)
            }
        }

        override fun loadTlb(cellSlice: CellSlice) =
            SnakeDataCons(
                cellSlice.loadBits(cellSlice.bits.size - cellSlice.bitsPosition),
                cellSlice.loadRef {
                    loadTlb(SnakeData)
                }
            )
    }
}
