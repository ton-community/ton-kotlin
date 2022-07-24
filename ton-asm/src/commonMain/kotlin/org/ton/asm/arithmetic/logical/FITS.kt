package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class FITS(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c FITS"

    companion object : TlbConstructorProvider<FITS> by FITSTlbConstructor
}

private object FITSTlbConstructor : TlbConstructor<FITS>(
    schema = "asm_fits#b4 c:uint8 = FITS;",
    type = FITS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: FITS) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): FITS {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return FITS(c)
    }
}
