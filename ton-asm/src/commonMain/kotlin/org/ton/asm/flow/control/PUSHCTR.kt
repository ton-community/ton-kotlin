package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHCTR(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c PUSHCTR"

    companion object : TlbConstructorProvider<PUSHCTR> by PUSHCTRTlbConstructor
}

private object PUSHCTRTlbConstructor : TlbConstructor<PUSHCTR>(
    schema = "asm_pushctr#ed4 c:uint4 = PUSHCTR;",
    type = PUSHCTR::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHCTR) {
        cellBuilder.storeUInt(value.c, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHCTR {
        val c = cellSlice.loadUInt(4).toInt()
        return PUSHCTR(c)
    }
}