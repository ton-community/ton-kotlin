package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QAND : AsmInstruction, TlbConstructorProvider<QAND> by QANDTlbConstructor {
    override fun toString(): String = "QAND"
}

private object QANDTlbConstructor : TlbConstructor<QAND>(
    schema = "asm_qand#b7b0 = QAND;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QAND) {
    }

    override fun loadTlb(cellSlice: CellSlice): QAND = QAND
}