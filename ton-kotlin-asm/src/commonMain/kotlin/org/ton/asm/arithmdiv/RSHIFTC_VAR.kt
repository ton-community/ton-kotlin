package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RSHIFTC_VAR : AsmInstruction, TlbConstructorProvider<RSHIFTC_VAR> by RSHIFTC_VARTlbConstructor {
    override fun toString(): String = "RSHIFTC_VAR"
}

private object RSHIFTC_VARTlbConstructor : TlbConstructor<RSHIFTC_VAR>(
    schema = "asm_rshiftc_var#a926 = RSHIFTC_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RSHIFTC_VAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): RSHIFTC_VAR = RSHIFTC_VAR
}
