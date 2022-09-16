package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QXOR : AsmInstruction, TlbConstructorProvider<QXOR> by QXORTlbConstructor {
    override fun toString(): String = "QXOR"
}

private object QXORTlbConstructor : TlbConstructor<QXOR>(
    schema = "asm_qxor#b7b2 = QXOR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QXOR) {
    }

    override fun loadTlb(cellSlice: CellSlice): QXOR = QXOR
}