package org.ton.asm.arithmlogical

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class FITS(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc+1u} FITS"

    companion object : TlbConstructorProvider<FITS> by FITSTlbConstructor
}

private object FITSTlbConstructor : TlbConstructor<FITS>(
    schema = "asm_fits#b4 cc:uint8 = FITS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: FITS) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): FITS {
        val cc = cellSlice.loadUInt(8).toUByte()
        return FITS(cc)
    }
}
