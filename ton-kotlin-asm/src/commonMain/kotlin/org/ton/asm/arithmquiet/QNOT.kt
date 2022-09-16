package org.ton.asm.arithmquiet

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QNOT : AsmInstruction, TlbConstructorProvider<QNOT> by QNOTTlbConstructor {
    override fun toString(): String = "QNOT"
}

private object QNOTTlbConstructor : TlbConstructor<QNOT>(
    schema = "asm_qnot#b7b3 = QNOT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QNOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): QNOT = QNOT
}
