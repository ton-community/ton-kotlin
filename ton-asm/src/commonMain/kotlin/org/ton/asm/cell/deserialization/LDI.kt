package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDI(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c LDI"

    companion object : TlbConstructorProvider<LDI> by LDITlbConstructor
}

private object LDITlbConstructor : TlbConstructor<LDI>(
    schema = "asm_ldi#d2 c:uint8 = LDI;",
    type = LDI::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDI) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDI {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return LDI(c)
    }
}
