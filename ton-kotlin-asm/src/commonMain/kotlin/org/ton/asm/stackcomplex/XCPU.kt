package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

public data class XCPU(
    val i: UByte,
    val j: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i s$j XCPU"

    public companion object : TlbConstructorProvider<XCPU> by XCPUTlbConstructor
}

private object XCPUTlbConstructor : TlbConstructor<XCPU>(
    schema = "asm_xcpu#51 i:uint4 j:uint4 = XCPU;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCPU) {
        cellBuilder.storeUInt(value.i.toInt(), 4)
        cellBuilder.storeUInt(value.j.toInt(), 4)
    }

    override fun loadTlb(cellSlice: CellSlice): XCPU {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        return XCPU(i, j)
    }
}
