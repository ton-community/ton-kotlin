package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDIX : Instruction, TlbConstructorProvider<LDIX> by LDIXTlbConstructor {
    override fun toString(): String = "LDIX"
}

private object LDIXTlbConstructor : TlbConstructor<LDIX>(
    schema = "asm_ldix#d700 = LDIX;",
    type = LDIX::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDIX) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDIX = LDIX
}