package org.ton.tlb.providers

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbNegatedCodec
import org.ton.tlb.TlbNegatedConstructor

interface TlbConstructorProvider<T : Any> : TlbProvider<T>, TlbCodec<T> {
    fun tlbConstructor(): TlbConstructor<T>

    override fun loadTlb(cellSlice: CellSlice): T = tlbConstructor().loadTlb(cellSlice)
    override fun storeTlb(cellBuilder: CellBuilder, value: T) {
        tlbConstructor().storeTlb(cellBuilder, value)
    }
}

interface TlbNegatedConstructorProvider<T : Any> : TlbProvider<T>, TlbNegatedCodec<T> {
    fun tlbConstructor(): TlbNegatedConstructor<T>

    override fun loadNegatedTlb(cellSlice: CellSlice): Pair<Int, T> =
        tlbConstructor().loadNegatedTlb(cellSlice)

    override fun loadTlb(cellSlice: CellSlice): T =
        tlbConstructor().loadTlb(cellSlice)

    override fun storeNegatedTlb(cellBuilder: CellBuilder, value: T): Int =
        tlbConstructor().storeNegatedTlb(cellBuilder, value)

    override fun storeTlb(cellBuilder: CellBuilder, value: T) =
        tlbConstructor().storeTlb(cellBuilder, value)
}
