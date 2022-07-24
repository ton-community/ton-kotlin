package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDIQ(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c PLDIQ"

    companion object : TlbConstructorProvider<PLDIQ> by PLDIQTlbConstructor
}

private object PLDIQTlbConstructor : TlbConstructor<PLDIQ>(
    schema = "asm_pldiq#d70e c:uint8 = PLDIQ;",
    type = PLDIQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDIQ) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDIQ {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return PLDIQ(c)
    }
}