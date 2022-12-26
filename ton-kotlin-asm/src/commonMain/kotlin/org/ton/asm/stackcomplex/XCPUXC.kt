package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class XCPUXC(
    val i: UByte,
    val j: UByte,
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i s$j s${k - 1u} XCPUXC"

    companion object : TlbConstructorProvider<XCPUXC> by XCPUXCTlbConstructor
}

private object XCPUXCTlbConstructor : TlbConstructor<XCPUXC>(
    schema = "asm_xcpuxc#542 i:uint4 j:uint4 k:uint4 = XCPUXC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCPUXC) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): XCPUXC {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        val k = cellSlice.loadUInt(4).toUByte()
        return XCPUXC(i, j, k)
    }
}
