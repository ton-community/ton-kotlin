package org.ton.asm.contloops

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UNTILEND : AsmInstruction, TlbConstructorProvider<UNTILEND> by UNTILENDTlbConstructor {
    override fun toString(): String = "UNTILEND"
}

private object UNTILENDTlbConstructor : TlbConstructor<UNTILEND>(
    schema = "asm_untilend#e7 = UNTILEND;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UNTILEND) {
    }

    override fun loadTlb(cellSlice: CellSlice): UNTILEND = UNTILEND
}
