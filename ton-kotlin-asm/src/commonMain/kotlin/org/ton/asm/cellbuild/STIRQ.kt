package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class STIRQ(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc+1u} STIRQ"

    companion object : TlbConstructorProvider<STIRQ> by STIRQTlbConstructor
}

private object STIRQTlbConstructor : TlbConstructor<STIRQ>(
    schema = "asm_stirq#cf0e cc:uint8 = STIRQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STIRQ) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): STIRQ {
        val cc = cellSlice.loadUInt(8).toUByte()
        return STIRQ(cc)
    }
}
