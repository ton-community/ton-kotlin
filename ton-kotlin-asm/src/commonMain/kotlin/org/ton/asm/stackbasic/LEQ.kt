package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LEQ : AsmInstruction, TlbConstructorProvider<LEQ> by LEQTlbConstructor {
    override fun toString(): String = "LEQ"
}

private object LEQTlbConstructor : TlbConstructor<LEQ>(
    schema = "asm_leq#bb = LEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): LEQ = LEQ
}