package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDI(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} PLDI"

    companion object : TlbConstructorProvider<PLDI> by PLDITlbConstructor
}

private object PLDITlbConstructor : TlbConstructor<PLDI>(
    schema = "asm_pldi#d70a cc:uint8 = PLDI;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDI) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDI {
        val cc = cellSlice.loadUInt(8).toUByte()
        return PLDI(cc)
    }
}
