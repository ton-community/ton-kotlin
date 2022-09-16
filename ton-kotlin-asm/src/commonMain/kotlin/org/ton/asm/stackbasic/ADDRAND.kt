package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ADDRAND : AsmInstruction, TlbConstructorProvider<ADDRAND> by ADDRANDTlbConstructor {
    override fun toString(): String = "ADDRAND"
}

private object ADDRANDTlbConstructor : TlbConstructor<ADDRAND>(
    schema = "asm_addrand#f815 = ADDRAND;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ADDRAND) {
    }

    override fun loadTlb(cellSlice: CellSlice): ADDRAND = ADDRAND
}