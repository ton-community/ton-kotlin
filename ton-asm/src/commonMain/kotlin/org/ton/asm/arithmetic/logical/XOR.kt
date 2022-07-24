package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object XOR : Instruction, TlbConstructorProvider<XOR> by XORTlbConstructor {
    override fun toString(): String = "OR"
}

private object XORTlbConstructor : TlbConstructor<XOR>(
    schema = "asm_xor#b2 = XOR;",
    type = XOR::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XOR) {
    }

    override fun loadTlb(cellSlice: CellSlice): XOR = XOR
}