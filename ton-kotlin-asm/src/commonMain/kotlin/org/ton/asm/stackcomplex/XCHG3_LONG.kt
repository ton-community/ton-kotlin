package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

public data class XCHG3_LONG(
    val i: UByte,
    val j: UByte,
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i s$j s$k XCHG3"

    public companion object : TlbConstructorProvider<XCHG3_LONG> by XCHG3_ALTTlbConstructor
}

private object XCHG3_ALTTlbConstructor : TlbConstructor<XCHG3_LONG>(
    schema = "asm_xchg3_long#540 i:uint4 j:uint4 k:uint4 = XCHG3_LONG;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHG3_LONG) {
        cellBuilder.storeUInt(value.i.toInt(), 4)
        cellBuilder.storeUInt(value.j.toInt(), 4)
        cellBuilder.storeUInt(value.k.toInt(), 4)
    }

    override fun loadTlb(cellSlice: CellSlice): XCHG3_LONG {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        val k = cellSlice.loadUInt(4).toUByte()
        return XCHG3_LONG(i, j, k)
    }
}
