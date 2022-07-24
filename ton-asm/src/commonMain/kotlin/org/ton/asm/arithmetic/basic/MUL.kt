package org.ton.asm.arithmetic.basic

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MUL : Instruction, TlbConstructorProvider<MUL> by MULTlbConstructor {
    override fun toString(): String = "MUL"
}

private object MULTlbConstructor : TlbConstructor<MUL>(
    schema = "asm_mul#a8 = MUL;",
    type = MUL::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MUL) {
    }

    override fun loadTlb(cellSlice: CellSlice): MUL = MUL
}