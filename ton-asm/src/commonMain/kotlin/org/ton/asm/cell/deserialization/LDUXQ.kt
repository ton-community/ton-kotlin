package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDUXQ : Instruction, TlbConstructorProvider<LDUXQ> by LDUXQTlbConstructor {
    override fun toString(): String = "LDUXQ"
}

private object LDUXQTlbConstructor : TlbConstructor<LDUXQ>(
    schema = "asm_lduxq#d705 = LDUXQ;",
    type = LDUXQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDUXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDUXQ = LDUXQ
}