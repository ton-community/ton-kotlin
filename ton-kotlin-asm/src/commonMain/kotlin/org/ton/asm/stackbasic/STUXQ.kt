package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STUXQ : AsmInstruction, TlbConstructorProvider<STUXQ> by STUXQTlbConstructor {
    override fun toString(): String = "STUXQ"
}

private object STUXQTlbConstructor : TlbConstructor<STUXQ>(
    schema = "asm_stuxq#cf05 = STUXQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STUXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): STUXQ = STUXQ
}