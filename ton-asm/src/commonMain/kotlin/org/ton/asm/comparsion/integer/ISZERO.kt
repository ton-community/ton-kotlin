package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ISZERO : Instruction, TlbConstructorProvider<ISZERO> by ISZEROTlbConstructor {
    override fun toString(): String = "ISZERO"
}

private object ISZEROTlbConstructor : TlbConstructor<ISZERO>(
    schema = "asm_iszero#c0000 = ISZERO;",
    type = ISZERO::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ISZERO) {
    }

    override fun loadTlb(cellSlice: CellSlice): ISZERO = ISZERO
}