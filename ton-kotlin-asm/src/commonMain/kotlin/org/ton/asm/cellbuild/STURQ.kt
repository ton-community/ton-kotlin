package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class STURQ(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc+1u} STURQ"

    companion object : TlbConstructorProvider<STURQ> by STURQTlbConstructor
}

private object STURQTlbConstructor : TlbConstructor<STURQ>(
    schema = "asm_sturq#cf0f cc:uint8 = STURQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STURQ) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): STURQ {
        val cc = cellSlice.loadUInt(8).toUByte()
        return STURQ(cc)
    }
}
