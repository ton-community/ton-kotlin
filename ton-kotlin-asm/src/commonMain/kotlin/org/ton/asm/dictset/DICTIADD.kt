package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIADD : AsmInstruction, TlbConstructorProvider<DICTIADD> by DICTIADDTlbConstructor {
    override fun toString(): String = "DICTIADD"
}

private object DICTIADDTlbConstructor : TlbConstructor<DICTIADD>(
    schema = "asm_dictiadd#f434 = DICTIADD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIADD) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIADD = DICTIADD
}
