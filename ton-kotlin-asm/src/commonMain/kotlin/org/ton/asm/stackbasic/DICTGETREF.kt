package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTGETREF : AsmInstruction, TlbConstructorProvider<DICTGETREF> by DICTGETREFTlbConstructor {
    override fun toString(): String = "DICTGETREF"
}

private object DICTGETREFTlbConstructor : TlbConstructor<DICTGETREF>(
    schema = "asm_dictgetref#f40b = DICTGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTGETREF = DICTGETREF
}