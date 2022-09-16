package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NEWC : AsmInstruction, TlbConstructorProvider<NEWC> by NEWCTlbConstructor {
    override fun toString(): String = "NEWC"
}

private object NEWCTlbConstructor : TlbConstructor<NEWC>(
    schema = "asm_newc#c8 = NEWC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NEWC) {
    }

    override fun loadTlb(cellSlice: CellSlice): NEWC = NEWC
}
