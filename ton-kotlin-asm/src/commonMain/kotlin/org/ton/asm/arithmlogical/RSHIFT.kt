package org.ton.asm.arithmlogical

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class RSHIFT(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} RSHIFT#"

    companion object : TlbConstructorProvider<RSHIFT> by RSHIFTTlbConstructor
}

private object RSHIFTTlbConstructor : TlbConstructor<RSHIFT>(
    schema = "asm_rshift#ab cc:uint8 = RSHIFT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RSHIFT) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): RSHIFT {
        val cc = cellSlice.loadUInt(8).toUByte()
        return RSHIFT(cc)
    }
}
