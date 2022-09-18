package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class MULRSHIFTR(
    val tt: UByte
) : AsmInstruction {
    override fun toString(): String = "${tt + 1u} MULRSHIFTR#"

    companion object : TlbConstructorProvider<MULRSHIFTR> by MULRSHIFTRTlbConstructor
}

private object MULRSHIFTRTlbConstructor : TlbConstructor<MULRSHIFTR>(
    schema = "asm_mulrshiftr#a9b5 tt:uint8 = MULRSHIFTR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MULRSHIFTR) {
        cellBuilder.storeUInt(value.tt, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): MULRSHIFTR {
        val tt = cellSlice.loadUInt(8).toUByte()
        return MULRSHIFTR(tt)
    }
}
