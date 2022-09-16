package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUSET : AsmInstruction, TlbConstructorProvider<DICTUSET> by DICTUSETTlbConstructor {
    override fun toString(): String = "DICTUSET"
}

private object DICTUSETTlbConstructor : TlbConstructor<DICTUSET>(
    schema = "asm_dictuset#f416 = DICTUSET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUSET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUSET = DICTUSET
}