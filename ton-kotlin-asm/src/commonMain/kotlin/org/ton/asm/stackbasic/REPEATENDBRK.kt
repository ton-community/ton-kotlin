package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object REPEATENDBRK : AsmInstruction, TlbConstructorProvider<REPEATENDBRK> by REPEATENDBRKTlbConstructor {
    override fun toString(): String = "REPEATENDBRK"
}

private object REPEATENDBRKTlbConstructor : TlbConstructor<REPEATENDBRK>(
    schema = "asm_repeatendbrk#e315 = REPEATENDBRK;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: REPEATENDBRK) {
    }

    override fun loadTlb(cellSlice: CellSlice): REPEATENDBRK = REPEATENDBRK
}