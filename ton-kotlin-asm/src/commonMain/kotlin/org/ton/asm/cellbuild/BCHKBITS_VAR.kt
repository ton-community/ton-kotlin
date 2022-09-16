package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BCHKBITS_VAR : AsmInstruction, TlbConstructorProvider<BCHKBITS_VAR> by BCHKBITS_VARTlbConstructor {
    override fun toString(): String = "BCHKBITS_VAR"
}

private object BCHKBITS_VARTlbConstructor : TlbConstructor<BCHKBITS_VAR>(
    schema = "asm_bchkbits_var#cf39 = BCHKBITS_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BCHKBITS_VAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): BCHKBITS_VAR = BCHKBITS_VAR
}
