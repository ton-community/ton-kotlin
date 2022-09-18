package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class BCHKBITSQ(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} BCHKBITSQ#"

    companion object : TlbConstructorProvider<BCHKBITSQ> by BCHKBITSQTlbConstructor
}

private object BCHKBITSQTlbConstructor : TlbConstructor<BCHKBITSQ>(
    schema = "asm_bchkbitsq#cf3c cc:uint8 = BCHKBITSQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BCHKBITSQ) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): BCHKBITSQ {
        val cc = cellSlice.loadUInt(8).toUByte()
        return BCHKBITSQ(cc)
    }
}
