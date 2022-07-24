package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDUX : Instruction, TlbConstructorProvider<LDUX> by LDUXTlbConstructor {
    override fun toString(): String = "LDUX"
}

private object LDUXTlbConstructor : TlbConstructor<LDUX>(
    schema = "asm_ldux#d701 = LDUX;",
    type = LDUX::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDUX) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDUX = LDUX
}