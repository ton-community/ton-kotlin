package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTADDGET : AsmInstruction, TlbConstructorProvider<DICTADDGET> by DICTADDGETTlbConstructor {
    override fun toString(): String = "DICTADDGET"
}

private object DICTADDGETTlbConstructor : TlbConstructor<DICTADDGET>(
    schema = "asm_dictaddget#f43a = DICTADDGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTADDGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTADDGET = DICTADDGET
}