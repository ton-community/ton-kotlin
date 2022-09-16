package org.ton.asm.dictget

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIGET : AsmInstruction, TlbConstructorProvider<DICTIGET> by DICTIGETTlbConstructor {
    override fun toString(): String = "DICTIGET"
}

private object DICTIGETTlbConstructor : TlbConstructor<DICTIGET>(
    schema = "asm_dictiget#f40c = DICTIGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIGET = DICTIGET
}
