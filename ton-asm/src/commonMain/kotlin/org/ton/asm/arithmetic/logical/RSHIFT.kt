package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RSHIFT : Instruction, TlbConstructorProvider<RSHIFT> by RSHIFTTlbConstructor {
    override fun toString(): String = "RSHIFT"
}

private object RSHIFTTlbConstructor : TlbConstructor<RSHIFT>(
    schema = "asm_rshift#ad = RSHIFT;",
    type = RSHIFT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RSHIFT) {
    }

    override fun loadTlb(cellSlice: CellSlice): RSHIFT = RSHIFT
}