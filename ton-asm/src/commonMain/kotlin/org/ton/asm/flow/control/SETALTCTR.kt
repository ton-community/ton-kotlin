package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SETALTCTR(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c SETALTCTR"

    companion object : TlbConstructorProvider<SETALTCTR> by SETALTCTRTlbConstructor
}

private object SETALTCTRTlbConstructor : TlbConstructor<SETALTCTR>(
    schema = "asm_setaltctr#ed8 c:uint4 = SETALTCTR;",
    type = SETALTCTR::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETALTCTR) {
        cellBuilder.storeUInt(value.c, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SETALTCTR {
        val c = cellSlice.loadUInt(4).toInt()
        return SETALTCTR(c)
    }
}

