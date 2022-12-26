package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class STI_ALT(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} STI_l"

    companion object : TlbConstructorProvider<STI_ALT> by STI_ALTTlbConstructor
}

private object STI_ALTTlbConstructor : TlbConstructor<STI_ALT>(
    schema = "asm_sti_alt#cf08 cc:uint8 = STI_ALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STI_ALT) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): STI_ALT {
        val cc = cellSlice.loadUInt(8).toUByte()
        return STI_ALT(cc)
    }
}
