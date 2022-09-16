package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STUXRQ : AsmInstruction, TlbConstructorProvider<STUXRQ> by STUXRQTlbConstructor {
    override fun toString(): String = "STUXRQ"
}

private object STUXRQTlbConstructor : TlbConstructor<STUXRQ>(
    schema = "asm_stuxrq#cf07 = STUXRQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STUXRQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): STUXRQ = STUXRQ
}
