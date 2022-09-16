package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUGETEXEC : AsmInstruction, TlbConstructorProvider<DICTUGETEXEC> by DICTUGETEXECTlbConstructor {
    override fun toString(): String = "DICTUGETEXEC"
}

private object DICTUGETEXECTlbConstructor : TlbConstructor<DICTUGETEXEC>(
    schema = "asm_dictugetexec#f4a3 = DICTUGETEXEC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUGETEXEC) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUGETEXEC = DICTUGETEXEC
}