package org.ton.tlb.providers

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

interface TlbConstructorProvider<T : Any> : TlbProvider<T>, TlbCodec<T> {
    fun tlbConstructor(): TlbConstructor<T>

    override fun loadTlb(cellSlice: CellSlice): T = tlbConstructor().loadTlb(cellSlice)
    override fun storeTlb(cellBuilder: CellBuilder, value: T) {
        tlbConstructor().storeTlb(cellBuilder, value)
    }
}