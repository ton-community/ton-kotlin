package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTSETGET : AsmInstruction, TlbConstructorProvider<DICTSETGET> by DICTSETGETTlbConstructor {
    override fun toString(): String = "DICTSETGET"
}

private object DICTSETGETTlbConstructor : TlbConstructor<DICTSETGET>(
    schema = "asm_dictsetget#f41a = DICTSETGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTSETGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTSETGET = DICTSETGET
}