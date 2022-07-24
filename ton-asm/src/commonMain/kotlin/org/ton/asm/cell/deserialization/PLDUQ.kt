package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDUQ(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c PLDUQ"

    companion object : TlbConstructorProvider<PLDUQ> by PLDUQTlbConstructor
}

private object PLDUQTlbConstructor : TlbConstructor<PLDUQ>(
    schema = "asm_plduq#d70f c:uint8 = PLDUQ;",
    type = PLDUQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDUQ) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDUQ {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return PLDUQ(c)
    }
}