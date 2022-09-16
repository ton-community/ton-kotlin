package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MULRSHIFTC_VAR : AsmInstruction, TlbConstructorProvider<MULRSHIFTC_VAR> by MULRSHIFTC_VARTlbConstructor {
    override fun toString(): String = "MULRSHIFTC_VAR"
}

private object MULRSHIFTC_VARTlbConstructor : TlbConstructor<MULRSHIFTC_VAR>(
    schema = "asm_mulrshiftc_var#a9a6 = MULRSHIFTC_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MULRSHIFTC_VAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): MULRSHIFTC_VAR = MULRSHIFTC_VAR
}
