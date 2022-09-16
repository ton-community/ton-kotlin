package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTDEL : AsmInstruction, TlbConstructorProvider<DICTDEL> by DICTDELTlbConstructor {
    override fun toString(): String = "DICTDEL"
}

private object DICTDELTlbConstructor : TlbConstructor<DICTDEL>(
    schema = "asm_dictdel#f459 = DICTDEL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTDEL) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTDEL = DICTDEL
}