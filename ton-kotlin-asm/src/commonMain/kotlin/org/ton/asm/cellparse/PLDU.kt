package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDU(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} PLDU"

    companion object : TlbConstructorProvider<PLDU> by PLDUTlbConstructor
}

private object PLDUTlbConstructor : TlbConstructor<PLDU>(
    schema = "asm_pldu#d70b cc:uint8 = PLDU;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDU) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDU {
        val cc = cellSlice.loadUInt(8).toUByte()
        return PLDU(cc)
    }
}
