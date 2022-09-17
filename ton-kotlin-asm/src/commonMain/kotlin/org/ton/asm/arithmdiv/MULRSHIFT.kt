package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class MULRSHIFT(
    val tt: UByte
) : AsmInstruction {
    override fun toString(): String = "${tt+1u} MULRSHIFT#"

    companion object : TlbConstructorProvider<MULRSHIFT> by MULRSHIFTTlbConstructor
}

private object MULRSHIFTTlbConstructor : TlbConstructor<MULRSHIFT>(
    schema = "asm_mulrshift#a9b4 tt:uint8 = MULRSHIFT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MULRSHIFT) {
        cellBuilder.storeUInt(value.tt, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): MULRSHIFT {
        val tt = cellSlice.loadUInt(8).toUByte()
        return MULRSHIFT(tt)
    }
}
