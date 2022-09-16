package org.ton.asm.arithmquiet

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QMOD : AsmInstruction, TlbConstructorProvider<QMOD> by QMODTlbConstructor {
    override fun toString(): String = "QMOD"
}

private object QMODTlbConstructor : TlbConstructor<QMOD>(
    schema = "asm_qmod#b7a908 = QMOD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QMOD) {
    }

    override fun loadTlb(cellSlice: CellSlice): QMOD = QMOD
}
