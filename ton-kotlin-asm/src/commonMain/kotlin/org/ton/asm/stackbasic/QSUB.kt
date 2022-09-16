package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QSUB : AsmInstruction, TlbConstructorProvider<QSUB> by QSUBTlbConstructor {
    override fun toString(): String = "QSUB"
}

private object QSUBTlbConstructor : TlbConstructor<QSUB>(
    schema = "asm_qsub#b7a1 = QSUB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QSUB) {
    }

    override fun loadTlb(cellSlice: CellSlice): QSUB = QSUB
}