package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETSECOND : AsmInstruction, TlbConstructorProvider<SETSECOND> by SETSECONDTlbConstructor {
    override fun toString(): String = "SETSECOND"
}

private object SETSECONDTlbConstructor : TlbConstructor<SETSECOND>(
    schema = "asm_setsecond#6f51 = SETSECOND;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETSECOND) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETSECOND = SETSECOND
}