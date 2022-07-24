package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDUQ(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c LDUQ"

    companion object : TlbConstructorProvider<LDUQ> by LDUQTlbConstructor
}

private object LDUQTlbConstructor : TlbConstructor<LDUQ>(
    schema = "asm_lduq#d70d c:uint8 = LDUQ;",
    type = LDUQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDUQ) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDUQ {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return LDUQ(c)
    }
}