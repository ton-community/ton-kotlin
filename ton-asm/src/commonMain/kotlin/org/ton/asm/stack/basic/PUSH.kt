package org.ton.asm.stack.basic

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSH(
    val s: Int
) : Instruction {
    override fun toString(): String = "$s PUSH"

    companion object : TlbConstructorProvider<PUSH> by PUSHTlbConstructor
}

private object PUSHTlbConstructor : TlbConstructor<PUSH>(
    schema = "asm_push#2 s:uint4 = PUSH;",
    type = PUSH::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSH) {
        cellBuilder.storeUInt(value.s, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSH {
        val s = cellSlice.loadUInt(4).toInt()
        return PUSH(s)
    }
}