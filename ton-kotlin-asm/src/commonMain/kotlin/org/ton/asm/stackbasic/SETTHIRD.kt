package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETTHIRD : AsmInstruction, TlbConstructorProvider<SETTHIRD> by SETTHIRDTlbConstructor {
    override fun toString(): String = "SETTHIRD"
}

private object SETTHIRDTlbConstructor : TlbConstructor<SETTHIRD>(
    schema = "asm_setthird#6f52 = SETTHIRD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETTHIRD) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETTHIRD = SETTHIRD
}