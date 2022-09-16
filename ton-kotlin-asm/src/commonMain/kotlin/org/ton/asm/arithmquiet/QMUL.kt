package org.ton.asm.arithmquiet

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QMUL : AsmInstruction, TlbConstructorProvider<QMUL> by QMULTlbConstructor {
    override fun toString(): String = "QMUL"
}

private object QMULTlbConstructor : TlbConstructor<QMUL>(
    schema = "asm_qmul#b7a8 = QMUL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QMUL) {
    }

    override fun loadTlb(cellSlice: CellSlice): QMUL = QMUL
}
