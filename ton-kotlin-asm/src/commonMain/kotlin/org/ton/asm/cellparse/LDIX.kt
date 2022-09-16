package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDIX : AsmInstruction, TlbConstructorProvider<LDIX> by LDIXTlbConstructor {
    override fun toString(): String = "LDIX"
}

private object LDIXTlbConstructor : TlbConstructor<LDIX>(
    schema = "asm_ldix#d700 = LDIX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDIX) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDIX = LDIX
}
