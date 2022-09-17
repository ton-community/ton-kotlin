package org.ton.asm.arithmlogical

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class UFITS(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc+1u} UFITS"

    companion object : TlbConstructorProvider<UFITS> by UFITSTlbConstructor
}

private object UFITSTlbConstructor : TlbConstructor<UFITS>(
    schema = "asm_ufits#b5 cc:uint8 = UFITS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UFITS) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): UFITS {
        val cc = cellSlice.loadUInt(8).toUByte()
        return UFITS(cc)
    }
}
