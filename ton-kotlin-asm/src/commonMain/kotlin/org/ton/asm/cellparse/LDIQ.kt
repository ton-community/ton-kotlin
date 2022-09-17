package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDIQ(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc+1u} LDIQ"

    companion object : TlbConstructorProvider<LDIQ> by LDIQTlbConstructor
}

private object LDIQTlbConstructor : TlbConstructor<LDIQ>(
    schema = "asm_ldiq#d70c cc:uint8 = LDIQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDIQ) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDIQ {
        val cc = cellSlice.loadUInt(8).toUByte()
        return LDIQ(cc)
    }
}
