package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDREF : AsmInstruction, TlbConstructorProvider<LDREF> by LDREFTlbConstructor {
    override fun toString(): String = "LDREF"
}

private object LDREFTlbConstructor : TlbConstructor<LDREF>(
    schema = "asm_ldref#d4 = LDREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDREF = LDREF
}