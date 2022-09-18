package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDUQ(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} LDUQ"

    companion object : TlbConstructorProvider<LDUQ> by LDUQTlbConstructor
}

private object LDUQTlbConstructor : TlbConstructor<LDUQ>(
    schema = "asm_lduq#d70d cc:uint8 = LDUQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDUQ) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDUQ {
        val cc = cellSlice.loadUInt(8).toUByte()
        return LDUQ(cc)
    }
}
