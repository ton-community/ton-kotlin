package org.ton.asm.dictdelete

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUDEL : AsmInstruction, TlbConstructorProvider<DICTUDEL> by DICTUDELTlbConstructor {
    override fun toString(): String = "DICTUDEL"
}

private object DICTUDELTlbConstructor : TlbConstructor<DICTUDEL>(
    schema = "asm_dictudel#f45b = DICTUDEL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUDEL) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUDEL = DICTUDEL
}
