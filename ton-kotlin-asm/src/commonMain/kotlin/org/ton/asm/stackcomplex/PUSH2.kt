package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSH2(
    val i: UByte,
    val j: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i s$j PUSH2"

    companion object : TlbConstructorProvider<PUSH2> by PUSH2TlbConstructor
}

private object PUSH2TlbConstructor : TlbConstructor<PUSH2>(
    schema = "asm_push2#53 i:uint4 j:uint4 = PUSH2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSH2) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSH2 {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        return PUSH2(i, j)
    }
}
