package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object WHILEEND : AsmInstruction, TlbConstructorProvider<WHILEEND> by WHILEENDTlbConstructor {
    override fun toString(): String = "WHILEEND"
}

private object WHILEENDTlbConstructor : TlbConstructor<WHILEEND>(
    schema = "asm_whileend#e9 = WHILEEND;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: WHILEEND) {
    }

    override fun loadTlb(cellSlice: CellSlice): WHILEEND = WHILEEND
}