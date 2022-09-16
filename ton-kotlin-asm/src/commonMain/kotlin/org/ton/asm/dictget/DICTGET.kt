package org.ton.asm.dictget

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTGET : AsmInstruction, TlbConstructorProvider<DICTGET> by DICTGETTlbConstructor {
    override fun toString(): String = "DICTGET"
}

private object DICTGETTlbConstructor : TlbConstructor<DICTGET>(
    schema = "asm_dictget#f40a = DICTGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTGET = DICTGET
}
