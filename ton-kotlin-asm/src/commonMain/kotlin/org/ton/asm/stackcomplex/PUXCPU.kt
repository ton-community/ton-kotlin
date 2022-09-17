package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUXCPU(
    val i: UByte,
    val j: UByte,
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i s${j-1u} s${k-1u} PUXCPU"

    companion object : TlbConstructorProvider<PUXCPU> by PUXCPUTlbConstructor
}

private object PUXCPUTlbConstructor : TlbConstructor<PUXCPU>(
    schema = "asm_puxcpu#545 i:uint4 j:uint4 k:uint4 = PUXCPU;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUXCPU) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): PUXCPU {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        val k = cellSlice.loadUInt(4).toUByte()
        return PUXCPU(i, j, k)
    }
}
