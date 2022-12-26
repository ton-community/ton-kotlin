package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDSLICE_ALT(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} LDSLICE_l"

    companion object : TlbConstructorProvider<LDSLICE_ALT> by LDSLICE_ALTTlbConstructor
}

private object LDSLICE_ALTTlbConstructor : TlbConstructor<LDSLICE_ALT>(
    schema = "asm_ldslice_alt#d71c cc:uint8 = LDSLICE_ALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDSLICE_ALT) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDSLICE_ALT {
        val cc = cellSlice.loadUInt(8).toUByte()
        return LDSLICE_ALT(cc)
    }
}
