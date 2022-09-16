package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CADR : AsmInstruction, TlbConstructorProvider<CADR> by CADRTlbConstructor {
    override fun toString(): String = "CADR"
}

private object CADRTlbConstructor : TlbConstructor<CADR>(
    schema = "asm_cadr#6fb4 = CADR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CADR) {
    }

    override fun loadTlb(cellSlice: CellSlice): CADR = CADR
}
