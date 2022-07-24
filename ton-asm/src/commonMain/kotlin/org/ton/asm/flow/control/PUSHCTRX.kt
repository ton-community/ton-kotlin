package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PUSHCTRX : Instruction, TlbConstructorProvider<PUSHCTRX> by PUSHCTRXTlbConstructor {
    override fun toString(): String = "PUSHCTRX"
}

private object PUSHCTRXTlbConstructor : TlbConstructor<PUSHCTRX>(
    schema = "asm_pushctrx#ede0 = PUSHCTRX;",
    type = PUSHCTRX::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHCTRX) {
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHCTRX = PUSHCTRX
}