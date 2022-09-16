package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDIXQ : AsmInstruction, TlbConstructorProvider<LDIXQ> by LDIXQTlbConstructor {
    override fun toString(): String = "LDIXQ"
}

private object LDIXQTlbConstructor : TlbConstructor<LDIXQ>(
    schema = "asm_ldixq#d704 = LDIXQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDIXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDIXQ = LDIXQ
}