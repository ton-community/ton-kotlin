package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

public data class XCHG3(
    val i: Int,
    val j: Int,
    val k: Int
) : AsmInstruction {
    override fun toString(): String = "s$i s$j s$k XCHG3"

    public companion object : TlbConstructorProvider<XCHG3> by XCHG3TlbConstructor
}

private object XCHG3TlbConstructor : TlbConstructor<XCHG3>(
    schema = "asm_xchg3#4 i:uint4 j:uint4 k:uint4 = XCHG3;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHG3) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): XCHG3 {
        val i = cellSlice.loadUInt(4).toInt()
        val j = cellSlice.loadUInt(4).toInt()
        val k = cellSlice.loadUInt(4).toInt()
        return XCHG3(i, j, k)
    }
}
