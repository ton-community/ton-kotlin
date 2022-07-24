package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LSHIFT : Instruction, TlbConstructorProvider<LSHIFT> by LSHIFTTlbConstructor {
    override fun toString(): String = "LSHIFT"
}

private object LSHIFTTlbConstructor : TlbConstructor<LSHIFT>(
    schema = "asm_lshift#ac = LSHIFT;",
    type = LSHIFT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LSHIFT) {
    }

    override fun loadTlb(cellSlice: CellSlice): LSHIFT = LSHIFT
}