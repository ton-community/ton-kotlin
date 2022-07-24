package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDUXQ : Instruction, TlbConstructorProvider<PLDUXQ> by PLDUXQTlbConstructor {
    override fun toString(): String = "PLDUXQ"
}

private object PLDUXQTlbConstructor : TlbConstructor<PLDUXQ>(
    schema = "asm_plduxq#d707 = PLDUXQ;",
    type = PLDUXQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDUXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDUXQ = PLDUXQ
}