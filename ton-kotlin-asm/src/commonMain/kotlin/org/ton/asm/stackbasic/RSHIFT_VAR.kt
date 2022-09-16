package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RSHIFT_VAR : AsmInstruction, TlbConstructorProvider<RSHIFT_VAR> by RSHIFT_VARTlbConstructor {
    override fun toString(): String = "RSHIFT_VAR"
}

private object RSHIFT_VARTlbConstructor : TlbConstructor<RSHIFT_VAR>(
    schema = "asm_rshift_var#ad = RSHIFT_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RSHIFT_VAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): RSHIFT_VAR = RSHIFT_VAR
}