package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BREMBITS : AsmInstruction, TlbConstructorProvider<BREMBITS> by BREMBITSTlbConstructor {
    override fun toString(): String = "BREMBITS"
}

private object BREMBITSTlbConstructor : TlbConstructor<BREMBITS>(
    schema = "asm_brembits#cf35 = BREMBITS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BREMBITS) {
    }

    override fun loadTlb(cellSlice: CellSlice): BREMBITS = BREMBITS
}