package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDU(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c LDU"

    companion object : TlbConstructorProvider<LDU> by LDUTlbConstructor
}

private object LDUTlbConstructor : TlbConstructor<LDU>(
    schema = "asm_ldu#d3 c:uint8 = LDU;",
    type = LDU::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDU) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDU {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return LDU(c)
    }
}
