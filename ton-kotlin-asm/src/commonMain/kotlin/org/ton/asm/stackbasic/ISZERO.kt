package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ISZERO : AsmInstruction, TlbConstructorProvider<ISZERO> by ISZEROTlbConstructor {
    override fun toString(): String = "ISZERO"
}

private object ISZEROTlbConstructor : TlbConstructor<ISZERO>(
    schema = "asm_iszero#c000 = ISZERO;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ISZERO) {
    }

    override fun loadTlb(cellSlice: CellSlice): ISZERO = ISZERO
}