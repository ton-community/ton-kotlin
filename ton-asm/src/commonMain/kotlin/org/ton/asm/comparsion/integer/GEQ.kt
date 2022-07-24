package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object GEQ : Instruction, TlbConstructorProvider<GEQ> by GEQTlbConstructor {
    override fun toString(): String = "GEQ"
}

private object GEQTlbConstructor : TlbConstructor<GEQ>(
    schema = "asm_geq#be = GEQ;",
    type = GEQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: GEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): GEQ = GEQ
}