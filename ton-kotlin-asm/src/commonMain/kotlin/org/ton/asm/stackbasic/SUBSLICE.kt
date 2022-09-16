package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SUBSLICE : AsmInstruction, TlbConstructorProvider<SUBSLICE> by SUBSLICETlbConstructor {
    override fun toString(): String = "SUBSLICE"
}

private object SUBSLICETlbConstructor : TlbConstructor<SUBSLICE>(
    schema = "asm_subslice#d734 = SUBSLICE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SUBSLICE) {
    }

    override fun loadTlb(cellSlice: CellSlice): SUBSLICE = SUBSLICE
}