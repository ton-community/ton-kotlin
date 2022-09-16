package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BBITS : AsmInstruction, TlbConstructorProvider<BBITS> by BBITSTlbConstructor {
    override fun toString(): String = "BBITS"
}

private object BBITSTlbConstructor : TlbConstructor<BBITS>(
    schema = "asm_bbits#cf31 = BBITS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BBITS) {
    }

    override fun loadTlb(cellSlice: CellSlice): BBITS = BBITS
}