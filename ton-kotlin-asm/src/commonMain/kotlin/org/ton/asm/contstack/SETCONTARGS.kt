package org.ton.asm.contstack

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SETCONTARGS(
    val r: UByte,
    val n: UByte = 15u
) : AsmInstruction {
    override fun toString(): String = "${r-1u} SETCONTARGS"

    companion object : TlbConstructorProvider<SETCONTARGS> by SETCONTARGSTlbConstructor
}

private object SETCONTARGSTlbConstructor : TlbConstructor<SETCONTARGS>(
    schema = "asm_setcontargs#ec r:uint4 n:(## 4) {n = 15} = SETCONTARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETCONTARGS) {
        cellBuilder.storeUInt(value.r, 4)
        cellBuilder.storeUInt(value.n, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SETCONTARGS {
        val r = cellSlice.loadUInt(4).toUByte()
        val n = cellSlice.loadUInt(4).toUByte()
        return SETCONTARGS(r, n)
    }
}
