package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SUBR : AsmInstruction, TlbConstructorProvider<SUBR> by SUBRTlbConstructor {
    override fun toString(): String = "SUBR"
}

private object SUBRTlbConstructor : TlbConstructor<SUBR>(
    schema = "asm_subr#a2 = SUBR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SUBR) {
    }

    override fun loadTlb(cellSlice: CellSlice): SUBR = SUBR
}