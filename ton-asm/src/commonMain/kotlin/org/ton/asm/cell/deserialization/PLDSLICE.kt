package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDSLICE(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c PLDSLICE"

    companion object : TlbConstructorProvider<PLDSLICE> by PLDSLICETlbConstructor
}

private object PLDSLICETlbConstructor : TlbConstructor<PLDSLICE>(
    schema = "asm_pldslice#d71d c:uint8 = PLDSLICE;",
    type = PLDSLICE::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDSLICE) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDSLICE {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return PLDSLICE(c)
    }
}