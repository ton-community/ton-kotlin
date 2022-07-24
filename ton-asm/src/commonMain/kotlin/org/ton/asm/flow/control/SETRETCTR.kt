package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SETRETCTR(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c SETRETCTR"

    companion object : TlbConstructorProvider<SETRETCTR> by SETRETCTRTlbConstructor
}

private object SETRETCTRTlbConstructor : TlbConstructor<SETRETCTR>(
    schema = "asm_setretctr#ed7 c:uint4 = SETRETCTR;",
    type = SETRETCTR::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETRETCTR) {
        cellBuilder.storeUInt(value.c, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SETRETCTR {
        val c = cellSlice.loadUInt(4).toInt()
        return SETRETCTR(c)
    }
}