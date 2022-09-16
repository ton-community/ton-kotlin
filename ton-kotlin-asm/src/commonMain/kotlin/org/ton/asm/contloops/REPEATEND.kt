package org.ton.asm.contloops

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object REPEATEND : AsmInstruction, TlbConstructorProvider<REPEATEND> by REPEATENDTlbConstructor {
    override fun toString(): String = "REPEATEND"
}

private object REPEATENDTlbConstructor : TlbConstructor<REPEATEND>(
    schema = "asm_repeatend#e5 = REPEATEND;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: REPEATEND) {
    }

    override fun loadTlb(cellSlice: CellSlice): REPEATEND = REPEATEND
}
