package org.ton.asm.dictcreate

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTEMPTY : AsmInstruction, TlbConstructorProvider<DICTEMPTY> by DICTEMPTYTlbConstructor {
    override fun toString(): String = "DICTEMPTY"
}

private object DICTEMPTYTlbConstructor : TlbConstructor<DICTEMPTY>(
    schema = "asm_dictempty#6e = DICTEMPTY;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTEMPTY) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTEMPTY = DICTEMPTY
}
