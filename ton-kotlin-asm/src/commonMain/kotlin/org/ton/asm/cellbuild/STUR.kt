package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class STUR(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc+1u} STUR"

    companion object : TlbConstructorProvider<STUR> by STURTlbConstructor
}

private object STURTlbConstructor : TlbConstructor<STUR>(
    schema = "asm_stur#cf0b cc:uint8 = STUR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STUR) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): STUR {
        val cc = cellSlice.loadUInt(8).toUByte()
        return STUR(cc)
    }
}
