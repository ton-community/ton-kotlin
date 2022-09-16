package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LSHIFTDIVR_VAR : AsmInstruction, TlbConstructorProvider<LSHIFTDIVR_VAR> by LSHIFTDIVR_VARTlbConstructor {
    override fun toString(): String = "LSHIFTDIVR_VAR"
}

private object LSHIFTDIVR_VARTlbConstructor : TlbConstructor<LSHIFTDIVR_VAR>(
    schema = "asm_lshiftdivr_var#a9c5 = LSHIFTDIVR_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LSHIFTDIVR_VAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): LSHIFTDIVR_VAR = LSHIFTDIVR_VAR
}