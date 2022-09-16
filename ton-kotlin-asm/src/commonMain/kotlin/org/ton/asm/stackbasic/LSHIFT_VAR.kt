package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LSHIFT_VAR : AsmInstruction, TlbConstructorProvider<LSHIFT_VAR> by LSHIFT_VARTlbConstructor {
    override fun toString(): String = "LSHIFT_VAR"
}

private object LSHIFT_VARTlbConstructor : TlbConstructor<LSHIFT_VAR>(
    schema = "asm_lshift_var#ac = LSHIFT_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LSHIFT_VAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): LSHIFT_VAR = LSHIFT_VAR
}