package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object JMPXDATA : AsmInstruction, TlbConstructorProvider<JMPXDATA> by JMPXDATATlbConstructor {
    override fun toString(): String = "JMPXDATA"
}

private object JMPXDATATlbConstructor : TlbConstructor<JMPXDATA>(
    schema = "asm_jmpxdata#db35 = JMPXDATA;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: JMPXDATA) {
    }

    override fun loadTlb(cellSlice: CellSlice): JMPXDATA = JMPXDATA
}