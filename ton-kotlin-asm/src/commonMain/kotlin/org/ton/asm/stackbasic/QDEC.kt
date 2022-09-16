package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QDEC : AsmInstruction, TlbConstructorProvider<QDEC> by QDECTlbConstructor {
    override fun toString(): String = "QDEC"
}

private object QDECTlbConstructor : TlbConstructor<QDEC>(
    schema = "asm_qdec#b7a5 = QDEC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QDEC) {
    }

    override fun loadTlb(cellSlice: CellSlice): QDEC = QDEC
}