package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDUL(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c LDUL"

    companion object : TlbConstructorProvider<LDUL> by LDULTlbConstructor
}

private object LDULTlbConstructor : TlbConstructor<LDUL>(
    schema = "asm_ldul#d709 c:uint8 = LDUL;",
    type = LDUL::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDUL) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDUL {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return LDUL(c)
    }
}