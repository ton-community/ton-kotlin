package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDREFRTOS : Instruction, TlbConstructorProvider<LDREFRTOS> by LDREFRTOSTlbConstructor {
    override fun toString(): String = "LDREFRTOS"
}

private object LDREFRTOSTlbConstructor : TlbConstructor<LDREFRTOS>(
    schema = "asm_ldrefrtos#d5 = LDREFRTOS;",
    type = LDREFRTOS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDREFRTOS) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDREFRTOS = LDREFRTOS
}