package org.ton.asm.configuration

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LTIME : Instruction, TlbConstructorProvider<LTIME> by LTIMETlbConstructor {
    override fun toString(): String = "LTIME"
}

private object LTIMETlbConstructor : TlbConstructor<LTIME>(
    schema = "asm_ltime#fb25 = LTIME;",
    type = LTIME::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LTIME) {
    }

    override fun loadTlb(cellSlice: CellSlice): LTIME = LTIME
}