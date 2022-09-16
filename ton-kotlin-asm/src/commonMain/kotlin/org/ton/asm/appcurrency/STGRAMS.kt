package org.ton.asm.appcurrency

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STGRAMS : AsmInstruction, TlbConstructorProvider<STGRAMS> by STGRAMSTlbConstructor {
    override fun toString(): String = "STGRAMS"
}

private object STGRAMSTlbConstructor : TlbConstructor<STGRAMS>(
    schema = "asm_stgrams#fa02 = STGRAMS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STGRAMS) {
    }

    override fun loadTlb(cellSlice: CellSlice): STGRAMS = STGRAMS
}
