package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class POPCTR(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "c$i POPCTR"

    companion object : TlbConstructorProvider<POPCTR> by POPCTRTlbConstructor
}

private object POPCTRTlbConstructor : TlbConstructor<POPCTR>(
    schema = "asm_popctr#ed5 i:uint4 = POPCTR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: POPCTR) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): POPCTR {
        val i = cellSlice.loadUInt(4).toUByte()
        return POPCTR(i)
    }
}
