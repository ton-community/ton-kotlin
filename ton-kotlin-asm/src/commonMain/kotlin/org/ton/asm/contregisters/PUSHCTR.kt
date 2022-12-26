package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHCTR(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "c$i PUSHCTR"

    companion object : TlbConstructorProvider<PUSHCTR> by PUSHCTRTlbConstructor
}

private object PUSHCTRTlbConstructor : TlbConstructor<PUSHCTR>(
    schema = "asm_pushctr#ed4 i:uint4 = PUSHCTR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHCTR) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHCTR {
        val i = cellSlice.loadUInt(4).toUByte()
        return PUSHCTR(i)
    }
}
