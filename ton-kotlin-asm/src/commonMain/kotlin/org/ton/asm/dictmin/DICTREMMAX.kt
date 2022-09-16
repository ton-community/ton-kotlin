package org.ton.asm.dictmin

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTREMMAX : AsmInstruction, TlbConstructorProvider<DICTREMMAX> by DICTREMMAXTlbConstructor {
    override fun toString(): String = "DICTREMMAX"
}

private object DICTREMMAXTlbConstructor : TlbConstructor<DICTREMMAX>(
    schema = "asm_dictremmax#f49a = DICTREMMAX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTREMMAX) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTREMMAX = DICTREMMAX
}
