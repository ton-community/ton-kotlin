package org.ton.tlb.providers

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.kotlin.cell.CellContext
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator

public interface TlbCombinatorProvider<T : Any> : TlbProvider<T>, TlbCodec<T> {
    public fun tlbCombinator(): TlbCombinator<T>

    override fun loadTlb(slice: CellSlice, context: CellContext): T =
        tlbCombinator().loadTlb(slice, context)

    override fun storeTlb(builder: CellBuilder, value: T, context: CellContext): Unit =
        tlbCombinator().storeTlb(builder, value, context)
}
