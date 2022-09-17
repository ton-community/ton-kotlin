package org.ton.asm.contcreate

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class BLESSARGS(
    val r: UByte,
    val n: UByte
) : AsmInstruction {
    override fun toString(): String = "$r $n BLESSARGS"

    companion object : TlbConstructorProvider<BLESSARGS> by BLESSARGSTlbConstructor
}

private object BLESSARGSTlbConstructor : TlbConstructor<BLESSARGS>(
    schema = "asm_blessargs#ee r:uint4 n:uint4 = BLESSARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BLESSARGS) {
        cellBuilder.storeUInt(value.r, 4)
        cellBuilder.storeUInt(value.n, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): BLESSARGS {
        val r = cellSlice.loadUInt(4).toUByte()
        val n = cellSlice.loadUInt(4).toUByte()
        return BLESSARGS(r, n)
    }
}
