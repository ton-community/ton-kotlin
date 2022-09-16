package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BCHKBITSQ_VAR : AsmInstruction, TlbConstructorProvider<BCHKBITSQ_VAR> by BCHKBITSQ_VARTlbConstructor {
    override fun toString(): String = "BCHKBITSQ_VAR"
}

private object BCHKBITSQ_VARTlbConstructor : TlbConstructor<BCHKBITSQ_VAR>(
    schema = "asm_bchkbitsq_var#cf3d = BCHKBITSQ_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BCHKBITSQ_VAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): BCHKBITSQ_VAR = BCHKBITSQ_VAR
}