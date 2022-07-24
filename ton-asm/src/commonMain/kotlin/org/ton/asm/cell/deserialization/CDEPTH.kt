package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CDEPTH : Instruction, TlbConstructorProvider<CDEPTH> by CDEPTHTlbConstructor {
    override fun toString(): String = "CDEPTH"
}

private object CDEPTHTlbConstructor : TlbConstructor<CDEPTH>(
    schema = "asm_cdepth#d765 = CDEPTH;",
    type = CDEPTH::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CDEPTH) {
    }

    override fun loadTlb(cellSlice: CellSlice): CDEPTH = CDEPTH
}