package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDUXQ : AsmInstruction, TlbConstructorProvider<LDUXQ> by LDUXQTlbConstructor {
    override fun toString(): String = "LDUXQ"
}

private object LDUXQTlbConstructor : TlbConstructor<LDUXQ>(
    schema = "asm_lduxq#d705 = LDUXQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDUXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDUXQ = LDUXQ
}
