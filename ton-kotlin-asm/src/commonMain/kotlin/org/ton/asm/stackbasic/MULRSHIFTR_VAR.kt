package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MULRSHIFTR_VAR : AsmInstruction, TlbConstructorProvider<MULRSHIFTR_VAR> by MULRSHIFTR_VARTlbConstructor {
    override fun toString(): String = "MULRSHIFTR_VAR"
}

private object MULRSHIFTR_VARTlbConstructor : TlbConstructor<MULRSHIFTR_VAR>(
    schema = "asm_mulrshiftr_var#a9a5 = MULRSHIFTR_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MULRSHIFTR_VAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): MULRSHIFTR_VAR = MULRSHIFTR_VAR
}