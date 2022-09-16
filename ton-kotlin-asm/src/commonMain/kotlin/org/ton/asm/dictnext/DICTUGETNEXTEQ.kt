package org.ton.asm.dictnext

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUGETNEXTEQ : AsmInstruction, TlbConstructorProvider<DICTUGETNEXTEQ> by DICTUGETNEXTEQTlbConstructor {
    override fun toString(): String = "DICTUGETNEXTEQ"
}

private object DICTUGETNEXTEQTlbConstructor : TlbConstructor<DICTUGETNEXTEQ>(
    schema = "asm_dictugetnexteq#f47d = DICTUGETNEXTEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUGETNEXTEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUGETNEXTEQ = DICTUGETNEXTEQ
}
