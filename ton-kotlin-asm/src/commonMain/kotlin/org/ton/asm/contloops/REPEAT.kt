package org.ton.asm.contloops

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object REPEAT : AsmInstruction, TlbConstructorProvider<REPEAT> by REPEATTlbConstructor {
    override fun toString(): String = "REPEAT"
}

private object REPEATTlbConstructor : TlbConstructor<REPEAT>(
    schema = "asm_repeat#e4 = REPEAT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: REPEAT) {
    }

    override fun loadTlb(cellSlice: CellSlice): REPEAT = REPEAT
}
