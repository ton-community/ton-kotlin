package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object XCTOS : Instruction, TlbConstructorProvider<XCTOS> by XCTOSTlbConstructor {
    override fun toString(): String = "XCTOS"
}

private object XCTOSTlbConstructor : TlbConstructor<XCTOS>(
    schema = "asm_xctos#d739 = XCTOS;",
    type = XCTOS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCTOS) {
    }

    override fun loadTlb(cellSlice: CellSlice): XCTOS = XCTOS
}