package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NEGATE : AsmInstruction, TlbConstructorProvider<NEGATE> by NEGATETlbConstructor {
    override fun toString(): String = "NEGATE"
}

private object NEGATETlbConstructor : TlbConstructor<NEGATE>(
    schema = "asm_negate#a3 = NEGATE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NEGATE) {
    }

    override fun loadTlb(cellSlice: CellSlice): NEGATE = NEGATE
}