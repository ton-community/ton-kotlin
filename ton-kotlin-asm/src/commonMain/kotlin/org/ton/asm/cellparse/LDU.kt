package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDU(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc+1u} LDU"

    companion object : TlbConstructorProvider<LDU> by LDUTlbConstructor
}

private object LDUTlbConstructor : TlbConstructor<LDU>(
    schema = "asm_ldu#d3 cc:uint8 = LDU;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDU) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDU {
        val cc = cellSlice.loadUInt(8).toUByte()
        return LDU(cc)
    }
}
