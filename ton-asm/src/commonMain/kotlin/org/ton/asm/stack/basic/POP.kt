package org.ton.asm.stack.basic

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class POP(
    val s: Int
) : Instruction {
    override fun toString(): String = "$s POP"

    companion object : TlbConstructorProvider<POP> by POPTlbConstructor
}

private object POPTlbConstructor : TlbConstructor<POP>(
    schema = "asm_pop#3 s:uint4 = POP;",
    type = POP::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: POP) {
        cellBuilder.storeUInt(value.s, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): POP {
        val s = cellSlice.loadUInt(4).toInt()
        return POP(s)
    }
}