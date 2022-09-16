package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THIRD : AsmInstruction, TlbConstructorProvider<THIRD> by THIRDTlbConstructor {
    override fun toString(): String = "THIRD"
}

private object THIRDTlbConstructor : TlbConstructor<THIRD>(
    schema = "asm_third#6f12 = THIRD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THIRD) {
    }

    override fun loadTlb(cellSlice: CellSlice): THIRD = THIRD
}