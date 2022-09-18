package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class STUQ(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} STUQ"

    companion object : TlbConstructorProvider<STUQ> by STUQTlbConstructor
}

private object STUQTlbConstructor : TlbConstructor<STUQ>(
    schema = "asm_stuq#cf0d cc:uint8 = STUQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STUQ) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): STUQ {
        val cc = cellSlice.loadUInt(8).toUByte()
        return STUQ(cc)
    }
}
