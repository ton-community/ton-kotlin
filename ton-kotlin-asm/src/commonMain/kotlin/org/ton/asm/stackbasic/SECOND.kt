package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SECOND : AsmInstruction, TlbConstructorProvider<SECOND> by SECONDTlbConstructor {
    override fun toString(): String = "SECOND"
}

private object SECONDTlbConstructor : TlbConstructor<SECOND>(
    schema = "asm_second#6f11 = SECOND;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SECOND) {
    }

    override fun loadTlb(cellSlice: CellSlice): SECOND = SECOND
}