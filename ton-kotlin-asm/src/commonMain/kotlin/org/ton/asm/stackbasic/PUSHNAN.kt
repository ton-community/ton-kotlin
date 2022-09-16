package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PUSHNAN : AsmInstruction, TlbConstructorProvider<PUSHNAN> by PUSHNANTlbConstructor {
    override fun toString(): String = "PUSHNAN"
}

private object PUSHNANTlbConstructor : TlbConstructor<PUSHNAN>(
    schema = "asm_pushnan#83ff = PUSHNAN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHNAN) {
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHNAN = PUSHNAN
}