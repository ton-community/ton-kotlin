package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class BCHKBITS(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} BCHKBITS#"

    companion object : TlbConstructorProvider<BCHKBITS> by BCHKBITSTlbConstructor
}

private object BCHKBITSTlbConstructor : TlbConstructor<BCHKBITS>(
    schema = "asm_bchkbits#cf38 cc:uint8 = BCHKBITS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BCHKBITS) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): BCHKBITS {
        val cc = cellSlice.loadUInt(8).toUByte()
        return BCHKBITS(cc)
    }
}
