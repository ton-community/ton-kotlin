package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CTOS : Instruction, TlbConstructorProvider<CTOS> by CTOSTlbConstructor {
    override fun toString(): String = "CTOS"
}

private object CTOSTlbConstructor : TlbConstructor<CTOS>(
    schema = "asm_ctos#d0 = CTOS;",
    type = CTOS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CTOS) {
    }

    override fun loadTlb(cellSlice: CellSlice): CTOS = CTOS
}