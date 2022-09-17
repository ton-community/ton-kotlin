package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class STIQ(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc+1u} STIQ"

    companion object : TlbConstructorProvider<STIQ> by STIQTlbConstructor
}

private object STIQTlbConstructor : TlbConstructor<STIQ>(
    schema = "asm_stiq#cf0c cc:uint8 = STIQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STIQ) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): STIQ {
        val cc = cellSlice.loadUInt(8).toUByte()
        return STIQ(cc)
    }
}
