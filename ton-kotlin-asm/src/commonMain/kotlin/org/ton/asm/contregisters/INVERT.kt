package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object INVERT : AsmInstruction, TlbConstructorProvider<INVERT> by INVERTTlbConstructor {
    override fun toString(): String = "INVERT"
}

private object INVERTTlbConstructor : TlbConstructor<INVERT>(
    schema = "asm_invert#edf8 = INVERT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: INVERT) {
    }

    override fun loadTlb(cellSlice: CellSlice): INVERT = INVERT
}
