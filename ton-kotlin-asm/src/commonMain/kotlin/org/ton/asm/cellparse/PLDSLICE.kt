package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDSLICE(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc+1u} PLDSLICE"

    companion object : TlbConstructorProvider<PLDSLICE> by PLDSLICETlbConstructor
}

private object PLDSLICETlbConstructor : TlbConstructor<PLDSLICE>(
    schema = "asm_pldslice#d71d cc:uint8 = PLDSLICE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDSLICE) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDSLICE {
        val cc = cellSlice.loadUInt(8).toUByte()
        return PLDSLICE(cc)
    }
}
