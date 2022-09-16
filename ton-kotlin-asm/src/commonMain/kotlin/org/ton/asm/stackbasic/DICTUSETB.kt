package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUSETB : AsmInstruction, TlbConstructorProvider<DICTUSETB> by DICTUSETBTlbConstructor {
    override fun toString(): String = "DICTUSETB"
}

private object DICTUSETBTlbConstructor : TlbConstructor<DICTUSETB>(
    schema = "asm_dictusetb#f443 = DICTUSETB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUSETB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUSETB = DICTUSETB
}