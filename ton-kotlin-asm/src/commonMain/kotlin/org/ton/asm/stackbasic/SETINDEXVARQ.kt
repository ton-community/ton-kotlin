package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETINDEXVARQ : AsmInstruction, TlbConstructorProvider<SETINDEXVARQ> by SETINDEXVARQTlbConstructor {
    override fun toString(): String = "SETINDEXVARQ"
}

private object SETINDEXVARQTlbConstructor : TlbConstructor<SETINDEXVARQ>(
    schema = "asm_setindexvarq#6f87 = SETINDEXVARQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETINDEXVARQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETINDEXVARQ = SETINDEXVARQ
}