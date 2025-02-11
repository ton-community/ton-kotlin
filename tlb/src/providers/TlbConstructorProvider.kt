package org.ton.tlb.providers

import org.ton.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

public interface TlbConstructorProvider<T : Any> : TlbCodec<T> {
    public fun tlbConstructor(): TlbConstructor<T>

    override fun storeTlb(builder: CellBuilder, value: T, context: CellContext) {
        tlbConstructor().storeTlb(builder, value)
    }

    override fun storeTlb(builder: CellBuilder, value: T) {
        tlbConstructor().storeTlb(builder, value, CellContext.EMPTY)
    }
}
