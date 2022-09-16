package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QTLEN : AsmInstruction, TlbConstructorProvider<QTLEN> by QTLENTlbConstructor {
    override fun toString(): String = "QTLEN"
}

private object QTLENTlbConstructor : TlbConstructor<QTLEN>(
    schema = "asm_qtlen#6f89 = QTLEN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QTLEN) {
    }

    override fun loadTlb(cellSlice: CellSlice): QTLEN = QTLEN
}