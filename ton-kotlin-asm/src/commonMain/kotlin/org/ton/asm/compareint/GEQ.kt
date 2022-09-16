package org.ton.asm.compareint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object GEQ : AsmInstruction, TlbConstructorProvider<GEQ> by GEQTlbConstructor {
    override fun toString(): String = "GEQ"
}

private object GEQTlbConstructor : TlbConstructor<GEQ>(
    schema = "asm_geq#be = GEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: GEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): GEQ = GEQ
}
