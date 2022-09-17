package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSH3(
    val i: UByte,
    val j: UByte,
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i s$j s$k PUSH3"

    companion object : TlbConstructorProvider<PUSH3> by PUSH3TlbConstructor
}

private object PUSH3TlbConstructor : TlbConstructor<PUSH3>(
    schema = "asm_push3#547 i:uint4 j:uint4 k:uint4 = PUSH3;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSH3) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSH3 {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        val k = cellSlice.loadUInt(4).toUByte()
        return PUSH3(i, j, k)
    }
}
