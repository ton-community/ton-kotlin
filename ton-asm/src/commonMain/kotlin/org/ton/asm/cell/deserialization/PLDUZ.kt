package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDUZ(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c PLDUZ"

    companion object : TlbConstructorProvider<PLDUZ> by PLDUZTlbConstructor
}

private object PLDUZTlbConstructor : TlbConstructor<PLDUZ>(
    schema = "asm_PLDUZ#d714_ c:uint3 data:(bits (32 * (c + 1))) = PLDUZ;",
    type = PLDUZ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDUZ) {
        cellBuilder.storeUInt(value.c / 32 - 1, 3)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDUZ {
        val c = (cellSlice.loadUInt(8).toInt() + 1) * 32
        return PLDUZ(c)
    }
}
