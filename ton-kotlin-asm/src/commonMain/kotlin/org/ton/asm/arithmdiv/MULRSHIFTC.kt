package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class MULRSHIFTC(
    val tt: UByte
) : AsmInstruction {
    override fun toString(): String = "${tt + 1u} MULRSHIFTC#"

    companion object : TlbConstructorProvider<MULRSHIFTC> by MULRSHIFTCTlbConstructor
}

private object MULRSHIFTCTlbConstructor : TlbConstructor<MULRSHIFTC>(
    schema = "asm_mulrshiftc#a9b6 tt:uint8 = MULRSHIFTC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MULRSHIFTC) {
        cellBuilder.storeUInt(value.tt, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): MULRSHIFTC {
        val tt = cellSlice.loadUInt(8).toUByte()
        return MULRSHIFTC(tt)
    }
}
