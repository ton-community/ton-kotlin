package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NIL : AsmInstruction, TlbConstructorProvider<NIL> by NILTlbConstructor {
    override fun toString(): String = "NIL"
}

private object NILTlbConstructor : TlbConstructor<NIL>(
    schema = "asm_nil#6f00 = NIL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NIL) {
    }

    override fun loadTlb(cellSlice: CellSlice): NIL = NIL
}
