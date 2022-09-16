package org.ton.asm.arithmquiet

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QADD : AsmInstruction, TlbConstructorProvider<QADD> by QADDTlbConstructor {
    override fun toString(): String = "QADD"
}

private object QADDTlbConstructor : TlbConstructor<QADD>(
    schema = "asm_qadd#b7a0 = QADD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QADD) {
    }

    override fun loadTlb(cellSlice: CellSlice): QADD = QADD
}
