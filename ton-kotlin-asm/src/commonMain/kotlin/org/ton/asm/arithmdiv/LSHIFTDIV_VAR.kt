package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LSHIFTDIV_VAR : AsmInstruction, TlbConstructorProvider<LSHIFTDIV_VAR> by LSHIFTDIV_VARTlbConstructor {
    override fun toString(): String = "LSHIFTDIV_VAR"
}

private object LSHIFTDIV_VARTlbConstructor : TlbConstructor<LSHIFTDIV_VAR>(
    schema = "asm_lshiftdiv_var#a9c4 = LSHIFTDIV_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LSHIFTDIV_VAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): LSHIFTDIV_VAR = LSHIFTDIV_VAR
}
