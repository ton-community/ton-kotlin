package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDI(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c PLDI"

    companion object : TlbConstructorProvider<PLDI> by PLDITlbConstructor
}

private object PLDITlbConstructor : TlbConstructor<PLDI>(
    schema = "asm_pldi#d70a c:uint8 = PLDI;",
    type = PLDI::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDI) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDI {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return PLDI(c)
    }
}