package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDI(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc+1u} LDI"

    companion object : TlbConstructorProvider<LDI> by LDITlbConstructor
}

private object LDITlbConstructor : TlbConstructor<LDI>(
    schema = "asm_ldi#d2 cc:uint8 = LDI;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDI) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDI {
        val cc = cellSlice.loadUInt(8).toUByte()
        return LDI(cc)
    }
}
