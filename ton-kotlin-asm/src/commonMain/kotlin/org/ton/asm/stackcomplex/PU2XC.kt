package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PU2XC(
    val i: UByte,
    val j: UByte,
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i s${j - 1u} s${k - 2u} PU2XC"

    companion object : TlbConstructorProvider<PU2XC> by PU2XCTlbConstructor
}

private object PU2XCTlbConstructor : TlbConstructor<PU2XC>(
    schema = "asm_pu2xc#546 i:uint4 j:uint4 k:uint4 = PU2XC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PU2XC) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): PU2XC {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        val k = cellSlice.loadUInt(4).toUByte()
        return PU2XC(i, j, k)
    }
}
