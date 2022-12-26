package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PUSHROOT : AsmInstruction, TlbConstructorProvider<PUSHROOT> by PUSHROOTTlbConstructor {
    override fun toString(): String = "c4 PUSHCTR"
}

private object PUSHROOTTlbConstructor : TlbConstructor<PUSHROOT>(
    schema = "asm_pushroot#ed44 = PUSHROOT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHROOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHROOT = PUSHROOT
}
