package org.ton.asm.dictdelete

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIDEL : AsmInstruction, TlbConstructorProvider<DICTIDEL> by DICTIDELTlbConstructor {
    override fun toString(): String = "DICTIDEL"
}

private object DICTIDELTlbConstructor : TlbConstructor<DICTIDEL>(
    schema = "asm_dictidel#f45a = DICTIDEL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIDEL) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIDEL = DICTIDEL
}
