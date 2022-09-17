package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SETRETCTR(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "c$i SETRETCTR"

    companion object : TlbConstructorProvider<SETRETCTR> by SETRETCTRTlbConstructor
}

private object SETRETCTRTlbConstructor : TlbConstructor<SETRETCTR>(
    schema = "asm_setretctr#ed7 i:uint4 = SETRETCTR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETRETCTR) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SETRETCTR {
        val i = cellSlice.loadUInt(4).toUByte()
        return SETRETCTR(i)
    }
}
