package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UNSINGLE : AsmInstruction, TlbConstructorProvider<UNSINGLE> by UNSINGLETlbConstructor {
    override fun toString(): String = "UNSINGLE"
}

private object UNSINGLETlbConstructor : TlbConstructor<UNSINGLE>(
    schema = "asm_unsingle#6f21 = UNSINGLE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UNSINGLE) {
    }

    override fun loadTlb(cellSlice: CellSlice): UNSINGLE = UNSINGLE
}