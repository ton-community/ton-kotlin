package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDUZ(
    val c: UByte
) : AsmInstruction {
    override fun toString(): String = "${32u * (c + 1u)} PLDUZ"

    companion object : TlbConstructorProvider<PLDUZ> by PLDUZTlbConstructor
}

private object PLDUZTlbConstructor : TlbConstructor<PLDUZ>(
    schema = "asm_plduz#d714_ c:uint3 = PLDUZ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDUZ) {
        cellBuilder.storeUInt(value.c, 3)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDUZ {
        val c = cellSlice.loadUInt(3).toUByte()
        return PLDUZ(c)
    }
}
