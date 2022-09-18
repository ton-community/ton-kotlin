package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDIQ(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} PLDIQ"

    companion object : TlbConstructorProvider<PLDIQ> by PLDIQTlbConstructor
}

private object PLDIQTlbConstructor : TlbConstructor<PLDIQ>(
    schema = "asm_pldiq#d70e cc:uint8 = PLDIQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDIQ) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDIQ {
        val cc = cellSlice.loadUInt(8).toUByte()
        return PLDIQ(cc)
    }
}
