package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SETALTCTR(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "c$i SETALTCTR"

    companion object : TlbConstructorProvider<SETALTCTR> by SETALTCTRTlbConstructor
}

private object SETALTCTRTlbConstructor : TlbConstructor<SETALTCTR>(
    schema = "asm_setaltctr#ed8 i:uint4 = SETALTCTR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETALTCTR) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SETALTCTR {
        val i = cellSlice.loadUInt(4).toUByte()
        return SETALTCTR(i)
    }
}
