package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LSHIFTDIVC_VAR : AsmInstruction, TlbConstructorProvider<LSHIFTDIVC_VAR> by LSHIFTDIVC_VARTlbConstructor {
    override fun toString(): String = "LSHIFTDIVC_VAR"
}

private object LSHIFTDIVC_VARTlbConstructor : TlbConstructor<LSHIFTDIVC_VAR>(
    schema = "asm_lshiftdivc_var#a9c6 = LSHIFTDIVC_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LSHIFTDIVC_VAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): LSHIFTDIVC_VAR = LSHIFTDIVC_VAR
}
