package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTSETB : AsmInstruction, TlbConstructorProvider<DICTSETB> by DICTSETBTlbConstructor {
    override fun toString(): String = "DICTSETB"
}

private object DICTSETBTlbConstructor : TlbConstructor<DICTSETB>(
    schema = "asm_dictsetb#f441 = DICTSETB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTSETB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTSETB = DICTSETB
}