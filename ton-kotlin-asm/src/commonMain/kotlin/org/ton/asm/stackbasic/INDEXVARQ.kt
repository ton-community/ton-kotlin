package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object INDEXVARQ : AsmInstruction, TlbConstructorProvider<INDEXVARQ> by INDEXVARQTlbConstructor {
    override fun toString(): String = "INDEXVARQ"
}

private object INDEXVARQTlbConstructor : TlbConstructor<INDEXVARQ>(
    schema = "asm_indexvarq#6f86 = INDEXVARQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: INDEXVARQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): INDEXVARQ = INDEXVARQ
}