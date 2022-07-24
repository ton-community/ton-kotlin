package org.ton.asm.flow.conditional

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFNOT : TlbConstructorProvider<IFNOT> by IFNOTTlbConstructor {
    override fun toString(): String = "IFNOT"
}

private object IFNOTTlbConstructor : TlbConstructor<IFNOT>(
    schema = "asm_ifnot#df = IFNOT;",
    type = IFNOT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFNOT = IFNOT
}