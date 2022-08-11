package org.ton.contract

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.loadRef
import org.ton.tlb.TlbNegatedCombinator
import org.ton.tlb.TlbNegatedConstructor
import org.ton.tlb.loadNegatedTlb
import org.ton.tlb.storeNegatedTlb

sealed interface SnakeData {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbNegatedCombinator<SnakeData> = SnakeDataTlbCombinator
    }

    private object SnakeDataTlbCombinator : TlbNegatedCombinator<SnakeData>() {
        private val snakeDataTail: TlbNegatedConstructor<SnakeDataTail> by lazy {
            SnakeDataTail.tlbCodec()
        }
        private val snakeDataCons: TlbNegatedConstructor<SnakeDataCons> by lazy {
            SnakeDataCons.tlbCodec()
        }
        override val constructors: List<TlbNegatedConstructor<out SnakeData>> by lazy {
            listOf(snakeDataCons, snakeDataTail)
        }

        override fun getConstructor(value: SnakeData): TlbNegatedConstructor<out SnakeData> = when (value) {
            is SnakeDataTail -> snakeDataTail
            is SnakeDataCons -> snakeDataCons
        }

        override fun loadTlb(cellSlice: CellSlice): SnakeData {
            return if (cellSlice.refs.lastIndex > cellSlice.refsPosition) {
                snakeDataCons.loadTlb(cellSlice) // More references available, this is a cons
            } else {
                snakeDataTail.loadTlb(cellSlice) // No more refs, this has to be a tail
            }
        }
    }
}

data class SnakeDataTail(
    val bits: BitString
) : SnakeData {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbNegatedConstructor<SnakeDataTail> = SnakeDataTailTlbConstructor
    }

    private object SnakeDataTailTlbConstructor : TlbNegatedConstructor<SnakeDataTail>(
        schema = "tail#_ {bn:#} b:(bits bn) = SnakeData ~0;"
    ) {
        override fun storeNegatedTlb(cellBuilder: CellBuilder, value: SnakeDataTail): Int {
            cellBuilder.storeBits(value.bits)
            return 0
        }

        override fun loadNegatedTlb(cellSlice: CellSlice): Pair<Int, SnakeDataTail> {
            val b = cellSlice.loadBits(cellSlice.bits.size - cellSlice.bitsPosition)
            return 0 to SnakeDataTail(b)
        }
    }
}

data class SnakeDataCons(
    val bits: BitString,
    val next: SnakeData
) : SnakeData {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbNegatedConstructor<SnakeDataCons> = SnakeDataConsTlbConstructor()
    }

    private class SnakeDataConsTlbConstructor : TlbNegatedConstructor<SnakeDataCons>(
        schema = "cons#_ {bn:#} {n:#} b:(bits bn) next:^(SnakeData ~n) = SnakeData ~(n + 1);"
    ) {
        private val snakeData: TlbNegatedCombinator<SnakeData> by lazy {
            SnakeData.tlbCodec()
        }

        override fun storeNegatedTlb(cellBuilder: CellBuilder, value: SnakeDataCons): Int {
            val remaining = cellBuilder.bits.size - cellBuilder.bitsPosition
            return if (remaining < value.bits.size) {
                val bits = value.bits.slice(0..remaining)
                val newBits = value.bits.slice(remaining + 1 until value.bits.size)
                val newValue = SnakeDataCons(bits, SnakeDataCons(newBits, value.next))
                val n = cellBuilder.storeNegatedTlb(snakeData, newValue)
                n + 1
            } else {
                cellBuilder.storeBits(value.bits)
                val n = cellBuilder.storeNegatedTlb(snakeData, value.next)
                n + 1
            }
        }

        override fun loadNegatedTlb(cellSlice: CellSlice): Pair<Int, SnakeDataCons> {
            val b = cellSlice.loadBits(cellSlice.bits.size - cellSlice.bitsPosition)
            val (n, next) = cellSlice.loadRef {
                cellSlice.loadNegatedTlb(snakeData)
            }
            return n + 1 to SnakeDataCons(b, next)
        }
    }
}
