package org.ton.asm.arithmquiet

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class QUFITS(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} QUFITS"

    companion object : TlbConstructorProvider<QUFITS> by QUFITSTlbConstructor
}

private object QUFITSTlbConstructor : TlbConstructor<QUFITS>(
    schema = "asm_qufits#b7b5 cc:uint8 = QUFITS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QUFITS) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): QUFITS {
        val cc = cellSlice.loadUInt(8).toUByte()
        return QUFITS(cc)
    }
}
