package org.ton.asm.dictnext

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTGETNEXTEQ : AsmInstruction, TlbConstructorProvider<DICTGETNEXTEQ> by DICTGETNEXTEQTlbConstructor {
    override fun toString(): String = "DICTGETNEXTEQ"
}

private object DICTGETNEXTEQTlbConstructor : TlbConstructor<DICTGETNEXTEQ>(
    schema = "asm_dictgetnexteq#f475 = DICTGETNEXTEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTGETNEXTEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTGETNEXTEQ = DICTGETNEXTEQ
}
