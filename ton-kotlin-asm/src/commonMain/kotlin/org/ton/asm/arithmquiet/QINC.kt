package org.ton.asm.arithmquiet

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QINC : AsmInstruction, TlbConstructorProvider<QINC> by QINCTlbConstructor {
    override fun toString(): String = "QINC"
}

private object QINCTlbConstructor : TlbConstructor<QINC>(
    schema = "asm_qinc#b7a4 = QINC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QINC) {
    }

    override fun loadTlb(cellSlice: CellSlice): QINC = QINC
}
