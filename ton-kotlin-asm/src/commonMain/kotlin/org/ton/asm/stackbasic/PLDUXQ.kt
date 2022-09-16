package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDUXQ : AsmInstruction, TlbConstructorProvider<PLDUXQ> by PLDUXQTlbConstructor {
    override fun toString(): String = "PLDUXQ"
}

private object PLDUXQTlbConstructor : TlbConstructor<PLDUXQ>(
    schema = "asm_plduxq#d707 = PLDUXQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDUXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDUXQ = PLDUXQ
}