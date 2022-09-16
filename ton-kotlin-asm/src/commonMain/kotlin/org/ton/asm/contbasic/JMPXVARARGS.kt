package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object JMPXVARARGS : AsmInstruction, TlbConstructorProvider<JMPXVARARGS> by JMPXVARARGSTlbConstructor {
    override fun toString(): String = "JMPXVARARGS"
}

private object JMPXVARARGSTlbConstructor : TlbConstructor<JMPXVARARGS>(
    schema = "asm_jmpxvarargs#db3a = JMPXVARARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: JMPXVARARGS) {
    }

    override fun loadTlb(cellSlice: CellSlice): JMPXVARARGS = JMPXVARARGS
}
