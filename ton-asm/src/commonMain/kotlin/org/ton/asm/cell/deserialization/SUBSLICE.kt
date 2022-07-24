package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SUBSLICE : Instruction, TlbConstructorProvider<SUBSLICE> by SUBSLICETlbConstructor {
    override fun toString(): String = "SUBSLICE"
}

private object SUBSLICETlbConstructor : TlbConstructor<SUBSLICE>(
    schema = "asm_subslice#d734 = SUBSLICE;",
    type = SUBSLICE::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SUBSLICE) {
    }

    override fun loadTlb(cellSlice: CellSlice): SUBSLICE = SUBSLICE
}