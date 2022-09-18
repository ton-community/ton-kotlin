package org.ton.asm.arithmquiet

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class QFITS(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} QFITS"

    companion object : TlbConstructorProvider<QFITS> by QFITSTlbConstructor
}

private object QFITSTlbConstructor : TlbConstructor<QFITS>(
    schema = "asm_qfits#b7b4 cc:uint8 = QFITS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QFITS) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): QFITS {
        val cc = cellSlice.loadUInt(8).toUByte()
        return QFITS(cc)
    }
}
