package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDI_ALT(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} LDI_l"

    companion object : TlbConstructorProvider<LDI_ALT> by LDI_ALTTlbConstructor
}

private object LDI_ALTTlbConstructor : TlbConstructor<LDI_ALT>(
    schema = "asm_ldi_alt#d708 cc:uint8 = LDI_ALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDI_ALT) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDI_ALT {
        val cc = cellSlice.loadUInt(8).toUByte()
        return LDI_ALT(cc)
    }
}
