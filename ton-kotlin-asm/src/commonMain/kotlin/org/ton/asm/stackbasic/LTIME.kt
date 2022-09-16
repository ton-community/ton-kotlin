package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LTIME : AsmInstruction, TlbConstructorProvider<LTIME> by LTIMETlbConstructor {
    override fun toString(): String = "LTIME"
}

private object LTIMETlbConstructor : TlbConstructor<LTIME>(
    schema = "asm_ltime#f825 = LTIME;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LTIME) {
    }

    override fun loadTlb(cellSlice: CellSlice): LTIME = LTIME
}