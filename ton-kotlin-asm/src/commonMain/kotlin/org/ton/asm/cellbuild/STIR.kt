package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class STIR(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} STIR"

    companion object : TlbConstructorProvider<STIR> by STIRTlbConstructor
}

private object STIRTlbConstructor : TlbConstructor<STIR>(
    schema = "asm_stir#cf0a cc:uint8 = STIR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STIR) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): STIR {
        val cc = cellSlice.loadUInt(8).toUByte()
        return STIR(cc)
    }
}
