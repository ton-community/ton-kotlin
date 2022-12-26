package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class XCHG3_ALT(
    val i: UByte,
    val j: UByte,
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i s$j s$k XCHG3_l"

    companion object : TlbConstructorProvider<XCHG3_ALT> by XCHG3_ALTTlbConstructor
}

private object XCHG3_ALTTlbConstructor : TlbConstructor<XCHG3_ALT>(
    schema = "asm_xchg3_alt#540 i:uint4 j:uint4 k:uint4 = XCHG3_ALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHG3_ALT) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): XCHG3_ALT {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        val k = cellSlice.loadUInt(4).toUByte()
        return XCHG3_ALT(i, j, k)
    }
}
