package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LEQ : Instruction, TlbConstructorProvider<LEQ> by LEQTlbConstructor {
    override fun toString(): String = "LEQ"
}

private object LEQTlbConstructor : TlbConstructor<LEQ>(
    schema = "asm_leq#bb = LEQ;",
    type = LEQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): LEQ = LEQ
}