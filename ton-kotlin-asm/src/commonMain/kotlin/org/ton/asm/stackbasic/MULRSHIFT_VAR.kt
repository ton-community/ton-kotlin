package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MULRSHIFT_VAR : AsmInstruction, TlbConstructorProvider<MULRSHIFT_VAR> by MULRSHIFT_VARTlbConstructor {
    override fun toString(): String = "MULRSHIFT_VAR"
}

private object MULRSHIFT_VARTlbConstructor : TlbConstructor<MULRSHIFT_VAR>(
    schema = "asm_mulrshift_var#a9a4 = MULRSHIFT_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MULRSHIFT_VAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): MULRSHIFT_VAR = MULRSHIFT_VAR
}