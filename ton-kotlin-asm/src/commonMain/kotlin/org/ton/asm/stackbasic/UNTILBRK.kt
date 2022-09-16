package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UNTILBRK : AsmInstruction, TlbConstructorProvider<UNTILBRK> by UNTILBRKTlbConstructor {
    override fun toString(): String = "UNTILBRK"
}

private object UNTILBRKTlbConstructor : TlbConstructor<UNTILBRK>(
    schema = "asm_untilbrk#e316 = UNTILBRK;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UNTILBRK) {
    }

    override fun loadTlb(cellSlice: CellSlice): UNTILBRK = UNTILBRK
}