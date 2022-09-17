package org.ton.asm.exceptions

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class TRYARGS(
    val p: UByte,
    val r: UByte
) : AsmInstruction {
    override fun toString(): String = "$p $r TRYARGS"

    companion object : TlbConstructorProvider<TRYARGS> by TRYARGSTlbConstructor
}

private object TRYARGSTlbConstructor : TlbConstructor<TRYARGS>(
    schema = "asm_tryargs#f3 p:uint4 r:uint4 = TRYARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TRYARGS) {
        cellBuilder.storeUInt(value.p, 4)
        cellBuilder.storeUInt(value.r, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): TRYARGS {
        val p = cellSlice.loadUInt(4).toUByte()
        val r = cellSlice.loadUInt(4).toUByte()
        return TRYARGS(p, r)
    }
}
