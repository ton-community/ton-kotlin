package org.ton.asm.arithmlogical

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CHKBOOL : AsmInstruction, TlbConstructorProvider<CHKBOOL> by CHKBOOLTlbConstructor {
    override fun toString(): String = "CHKBOOL"
}

private object CHKBOOLTlbConstructor : TlbConstructor<CHKBOOL>(
    schema = "asm_chkbool#b400 = CHKBOOL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CHKBOOL) {
    }

    override fun loadTlb(cellSlice: CellSlice): CHKBOOL = CHKBOOL
}
