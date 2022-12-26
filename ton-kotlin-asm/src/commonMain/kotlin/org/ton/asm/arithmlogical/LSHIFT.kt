package org.ton.asm.arithmlogical

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LSHIFT(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} LSHIFT#"

    companion object : TlbConstructorProvider<LSHIFT> by LSHIFTTlbConstructor
}

private object LSHIFTTlbConstructor : TlbConstructor<LSHIFT>(
    schema = "asm_lshift#aa cc:uint8 = LSHIFT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LSHIFT) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LSHIFT {
        val cc = cellSlice.loadUInt(8).toUByte()
        return LSHIFT(cc)
    }
}
