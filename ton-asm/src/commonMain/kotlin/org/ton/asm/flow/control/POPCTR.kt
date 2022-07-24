package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class POPCTR(
    val c: Int
) : Instruction {
    override fun toString(): String = "c$c POPCTR"

    companion object : TlbConstructorProvider<POPCTR> by POPCTRTlbConstructor
}

private object POPCTRTlbConstructor : TlbConstructor<POPCTR>(
    schema = "asm_popctr#ed5 c:uint4 = POPCTR;",
    type = POPCTR::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: POPCTR) {
        cellBuilder.storeUInt(value.c, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): POPCTR {
        val c = cellSlice.loadUInt(4).toInt()
        return POPCTR(c)
    }
}