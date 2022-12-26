package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SETCONTCTR(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "c$i SETCONTCTR"

    companion object : TlbConstructorProvider<SETCONTCTR> by SETCONTCTRTlbConstructor
}

private object SETCONTCTRTlbConstructor : TlbConstructor<SETCONTCTR>(
    schema = "asm_setcontctr#ed6 i:uint4 = SETCONTCTR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETCONTCTR) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SETCONTCTR {
        val i = cellSlice.loadUInt(4).toUByte()
        return SETCONTCTR(i)
    }
}
