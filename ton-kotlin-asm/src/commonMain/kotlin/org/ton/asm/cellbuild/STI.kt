package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class STI(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} STI"

    companion object : TlbConstructorProvider<STI> by STITlbConstructor
}

private object STITlbConstructor : TlbConstructor<STI>(
    schema = "asm_sti#ca cc:uint8 = STI;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STI) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): STI {
        val cc = cellSlice.loadUInt(8).toUByte()
        return STI(cc)
    }
}
