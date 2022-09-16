package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MULDIV : AsmInstruction, TlbConstructorProvider<MULDIV> by MULDIVTlbConstructor {
    override fun toString(): String = "MULDIV"
}

private object MULDIVTlbConstructor : TlbConstructor<MULDIV>(
    schema = "asm_muldiv#a984 = MULDIV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MULDIV) {
    }

    override fun loadTlb(cellSlice: CellSlice): MULDIV = MULDIV
}