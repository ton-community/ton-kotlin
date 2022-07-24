package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class UFITS(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c UFITS"

    companion object : TlbConstructorProvider<UFITS> by UFITSTlbConstructor
}

private object UFITSTlbConstructor : TlbConstructor<UFITS>(
    schema = "asm_ufits#b5 c:uint8 = UFITS;",
    type = UFITS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UFITS) {
        cellBuilder.storeUInt(value.c, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): UFITS {
        val c = cellSlice.loadUInt(8).toInt()
        return UFITS(c)
    }
}