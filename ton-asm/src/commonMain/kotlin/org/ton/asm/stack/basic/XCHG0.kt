package org.ton.asm.stack.basic

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class XCHG0(
    val s: Int
) : Instruction {
    override fun toString(): String = "$s XCHG0"

    companion object : TlbConstructorProvider<XCHG0> by XCH0TlbConstructor
}

private object XCH0TlbConstructor : TlbConstructor<XCHG0>(
    schema = "asm_xch0#0 s:uint4 = XCHG0;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHG0) {
        cellBuilder.storeUInt(value.s, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): XCHG0 {
        val s = cellSlice.loadUInt(4).toInt()
        return XCHG0(s)
    }
}