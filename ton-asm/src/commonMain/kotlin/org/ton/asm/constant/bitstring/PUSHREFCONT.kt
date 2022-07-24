package org.ton.asm.constant.bitstring

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PUSHREFCONT : Instruction, TlbConstructorProvider<PUSHREFCONT> by PUSHREFCONTTlbConstructor {
    override fun toString(): String = "PUSHREFCONT"
}

private object PUSHREFCONTTlbConstructor : TlbConstructor<PUSHREFCONT>(
    schema = "asm_pushrefcont#8a = PUSHREFCONT;",
    type = PUSHREFCONT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHREFCONT) {
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHREFCONT = PUSHREFCONT
}