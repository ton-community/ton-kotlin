package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUXC2(
    val i: UByte,
    val j: UByte,
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i s${j - 1u} s${k - 1u} PUXC2"

    companion object : TlbConstructorProvider<PUXC2> by PUXC2TlbConstructor
}

private object PUXC2TlbConstructor : TlbConstructor<PUXC2>(
    schema = "asm_puxc2#544 i:uint4 j:uint4 k:uint4 = PUXC2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUXC2) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): PUXC2 {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        val k = cellSlice.loadUInt(4).toUByte()
        return PUXC2(i, j, k)
    }
}
