package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDUQ(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} PLDUQ"

    companion object : TlbConstructorProvider<PLDUQ> by PLDUQTlbConstructor
}

private object PLDUQTlbConstructor : TlbConstructor<PLDUQ>(
    schema = "asm_plduq#d70f cc:uint8 = PLDUQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDUQ) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDUQ {
        val cc = cellSlice.loadUInt(8).toUByte()
        return PLDUQ(cc)
    }
}
