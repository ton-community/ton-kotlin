package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDSLICE(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} LDSLICE"

    companion object : TlbConstructorProvider<LDSLICE> by LDSLICETlbConstructor
}

private object LDSLICETlbConstructor : TlbConstructor<LDSLICE>(
    schema = "asm_ldslice#d6 cc:uint8 = LDSLICE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDSLICE) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDSLICE {
        val cc = cellSlice.loadUInt(8).toUByte()
        return LDSLICE(cc)
    }
}
