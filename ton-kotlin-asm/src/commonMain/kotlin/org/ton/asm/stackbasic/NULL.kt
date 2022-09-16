package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NULL : AsmInstruction, TlbConstructorProvider<NULL> by NULLTlbConstructor {
    override fun toString(): String = "NULL"
}

private object NULLTlbConstructor : TlbConstructor<NULL>(
    schema = "asm_null#6d = NULL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NULL) {
    }

    override fun loadTlb(cellSlice: CellSlice): NULL = NULL
}