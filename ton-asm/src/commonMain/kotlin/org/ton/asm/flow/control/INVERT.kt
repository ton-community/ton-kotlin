package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object INVERT : Instruction, TlbConstructorProvider<INVERT> by INVERTTlbConstructor {
    override fun toString(): String = "INVERT"
}

private object INVERTTlbConstructor : TlbConstructor<INVERT>(
    schema = "asm_invert#edf8 = INVERT;",
    type = INVERT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: INVERT) {
    }

    override fun loadTlb(cellSlice: CellSlice): INVERT = INVERT
}