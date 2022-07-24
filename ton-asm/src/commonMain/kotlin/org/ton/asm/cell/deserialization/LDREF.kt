package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDREF : Instruction, TlbConstructorProvider<LDREF> by LDREFTlbConstructor {
    override fun toString(): String = "LDREF"
}

private object LDREFTlbConstructor : TlbConstructor<LDREF>(
    schema = "asm_ldref#d4 = LDREF;",
    type = LDREF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDREF = LDREF
}