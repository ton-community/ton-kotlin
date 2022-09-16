package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QNEGATE : AsmInstruction, TlbConstructorProvider<QNEGATE> by QNEGATETlbConstructor {
    override fun toString(): String = "QNEGATE"
}

private object QNEGATETlbConstructor : TlbConstructor<QNEGATE>(
    schema = "asm_qnegate#b7a3 = QNEGATE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QNEGATE) {
    }

    override fun loadTlb(cellSlice: CellSlice): QNEGATE = QNEGATE
}