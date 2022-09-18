package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class XCHG_IJ(
    val i: UByte,
    val j: UByte,
) : AsmInstruction {
    init {
        require(i >= 1u) { "expected i >= 1, actual $i" }
        require(j >= i + 1u) { "expected j >= ${i + 1u}, actual $j" }
    }

    override fun toString(): String = "s$i s$j XCHG"

    companion object : TlbConstructorProvider<XCHG_IJ> by XCHG_IJTlbConstructor
}

private object XCHG_IJTlbConstructor : TlbConstructor<XCHG_IJ>(
    schema = "asm_xchg_ij#10 i:(## 4) j:(## 4) {1 <= i} {i + 1 <= j} = XCHG_IJ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHG_IJ) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): XCHG_IJ {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        return XCHG_IJ(i, j)
    }
}
