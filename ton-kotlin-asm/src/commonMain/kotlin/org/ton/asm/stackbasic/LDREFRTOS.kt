package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDREFRTOS : AsmInstruction, TlbConstructorProvider<LDREFRTOS> by LDREFRTOSTlbConstructor {
    override fun toString(): String = "LDREFRTOS"
}

private object LDREFRTOSTlbConstructor : TlbConstructor<LDREFRTOS>(
    schema = "asm_ldrefrtos#d5 = LDREFRTOS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDREFRTOS) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDREFRTOS = LDREFRTOS
}