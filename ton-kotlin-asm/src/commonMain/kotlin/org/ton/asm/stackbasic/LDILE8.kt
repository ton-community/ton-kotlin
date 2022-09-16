package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDILE8 : AsmInstruction, TlbConstructorProvider<LDILE8> by LDILE8TlbConstructor {
    override fun toString(): String = "LDILE8"
}

private object LDILE8TlbConstructor : TlbConstructor<LDILE8>(
    schema = "asm_ldile8#d752 = LDILE8;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDILE8) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDILE8 = LDILE8
}