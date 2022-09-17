package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class XCPU2(
    val i: UByte,
    val j: UByte,
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i s$j s$k XCPU2"

    companion object : TlbConstructorProvider<XCPU2> by XCPU2TlbConstructor
}

private object XCPU2TlbConstructor : TlbConstructor<XCPU2>(
    schema = "asm_xcpu2#543 i:uint4 j:uint4 k:uint4 = XCPU2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCPU2) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): XCPU2 {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        val k = cellSlice.loadUInt(4).toUByte()
        return XCPU2(i, j, k)
    }
}
