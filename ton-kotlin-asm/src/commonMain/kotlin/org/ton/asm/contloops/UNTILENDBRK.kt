package org.ton.asm.contloops

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UNTILENDBRK : AsmInstruction, TlbConstructorProvider<UNTILENDBRK> by UNTILENDBRKTlbConstructor {
    override fun toString(): String = "UNTILENDBRK"
}

private object UNTILENDBRKTlbConstructor : TlbConstructor<UNTILENDBRK>(
    schema = "asm_untilendbrk#e317 = UNTILENDBRK;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UNTILENDBRK) {
    }

    override fun loadTlb(cellSlice: CellSlice): UNTILENDBRK = UNTILENDBRK
}
