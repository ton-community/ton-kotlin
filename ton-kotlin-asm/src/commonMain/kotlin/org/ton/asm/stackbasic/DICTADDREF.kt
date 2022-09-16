package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTADDREF : AsmInstruction, TlbConstructorProvider<DICTADDREF> by DICTADDREFTlbConstructor {
    override fun toString(): String = "DICTADDREF"
}

private object DICTADDREFTlbConstructor : TlbConstructor<DICTADDREF>(
    schema = "asm_dictaddref#f433 = DICTADDREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTADDREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTADDREF = DICTADDREF
}