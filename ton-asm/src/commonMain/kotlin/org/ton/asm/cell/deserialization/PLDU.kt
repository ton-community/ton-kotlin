package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDU(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c PLDU"

    companion object : TlbConstructorProvider<PLDU> by PLDUTlbConstructor
}

private object PLDUTlbConstructor : TlbConstructor<PLDU>(
    schema = "asm_pldu#d70b c:uint8 = PLDU;",
    type = PLDU::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDU) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDU {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return PLDU(c)
    }
}