package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QOR : AsmInstruction, TlbConstructorProvider<QOR> by QORTlbConstructor {
    override fun toString(): String = "QOR"
}

private object QORTlbConstructor : TlbConstructor<QOR>(
    schema = "asm_qor#b7b1 = QOR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QOR) {
    }

    override fun loadTlb(cellSlice: CellSlice): QOR = QOR
}