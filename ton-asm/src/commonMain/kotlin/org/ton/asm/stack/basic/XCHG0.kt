package org.ton.asm.stack.basic

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

data class XCHG0(
    val s: Int
) {
    companion object {
        fun tlbConstructor(): TlbConstructor<XCHG0> = XCH0TlbConstructor
    }
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