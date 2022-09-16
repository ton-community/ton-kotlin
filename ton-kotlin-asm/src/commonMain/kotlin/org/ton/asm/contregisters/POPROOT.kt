package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object POPROOT : AsmInstruction, TlbConstructorProvider<POPROOT> by POPROOTTlbConstructor {
    override fun toString(): String = "POPROOT"
}

private object POPROOTTlbConstructor : TlbConstructor<POPROOT>(
    schema = "asm_poproot#ed54 = POPROOT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: POPROOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): POPROOT = POPROOT
}
