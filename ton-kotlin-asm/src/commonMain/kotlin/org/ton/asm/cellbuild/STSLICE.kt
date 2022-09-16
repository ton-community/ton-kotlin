package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STSLICE : AsmInstruction, TlbConstructorProvider<STSLICE> by STSLICETlbConstructor {
    override fun toString(): String = "STSLICE"
}

private object STSLICETlbConstructor : TlbConstructor<STSLICE>(
    schema = "asm_stslice#ce = STSLICE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STSLICE) {
    }

    override fun loadTlb(cellSlice: CellSlice): STSLICE = STSLICE
}
