package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PUSHCTRX : AsmInstruction, TlbConstructorProvider<PUSHCTRX> by PUSHCTRXTlbConstructor {
    override fun toString(): String = "PUSHCTRX"
}

private object PUSHCTRXTlbConstructor : TlbConstructor<PUSHCTRX>(
    schema = "asm_pushctrx#ede0 = PUSHCTRX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHCTRX) {
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHCTRX = PUSHCTRX
}
