package org.ton.tlb.providers

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator

public interface TlbCombinatorProvider<T : Any> : TlbProvider<T>, TlbCodec<T> {
    public fun tlbCombinator(): TlbCombinator<T>

    override fun loadTlb(cellSlice: CellSlice): T = tlbCombinator().loadTlb(cellSlice)
    override fun storeTlb(cellBuilder: CellBuilder, value: T) {
        tlbCombinator().storeTlb(cellBuilder, value)
    }
}
