package org.ton.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice

abstract class TlbNegatedCombinator<T> : AbstractTlbCombinator<T, TlbNegatedConstructor<T>>(), TlbNegatedCodec<T> {
    abstract override val constructors: List<TlbNegatedConstructor<out T>>

    abstract override fun getConstructor(value: T): TlbNegatedConstructor<out T>

    override fun storeNegatedTlb(cellBuilder: CellBuilder, value: T): Int =
        storeTlbConstructor(cellBuilder, value).storeNegatedTlb(cellBuilder, value)

    override fun loadNegatedTlb(cellSlice: CellSlice): Pair<Int, T> =
        loadTlbConstructor(cellSlice).loadNegatedTlb(cellSlice)
}