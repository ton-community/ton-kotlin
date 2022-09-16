package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object JMPX : AsmInstruction, TlbConstructorProvider<JMPX> by JMPXTlbConstructor {
    override fun toString(): String = "JMPX"
}

private object JMPXTlbConstructor : TlbConstructor<JMPX>(
    schema = "asm_jmpx#d9 = JMPX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: JMPX) {
    }

    override fun loadTlb(cellSlice: CellSlice): JMPX = JMPX
}
