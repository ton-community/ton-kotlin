package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

public data class PUXC(
    val i: UByte,
    val j: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i s$j PUXC"

    public companion object : TlbConstructorProvider<PUXC> by PUXCTlbConstructor
}

private object PUXCTlbConstructor : TlbConstructor<PUXC>(
    schema = "asm_puxc#52 i:uint4 j:uint4 = PUXC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUXC) {
        cellBuilder.storeUInt(value.i.toInt(), 4)
        cellBuilder.storeUInt(value.j.toInt(), 4)
    }

    override fun loadTlb(cellSlice: CellSlice): PUXC {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        return PUXC(i, j)
    }
}
