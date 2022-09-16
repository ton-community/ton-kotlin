package org.ton.asm.dictnext

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIGETNEXTEQ : AsmInstruction, TlbConstructorProvider<DICTIGETNEXTEQ> by DICTIGETNEXTEQTlbConstructor {
    override fun toString(): String = "DICTIGETNEXTEQ"
}

private object DICTIGETNEXTEQTlbConstructor : TlbConstructor<DICTIGETNEXTEQ>(
    schema = "asm_dictigetnexteq#f479 = DICTIGETNEXTEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIGETNEXTEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIGETNEXTEQ = DICTIGETNEXTEQ
}
