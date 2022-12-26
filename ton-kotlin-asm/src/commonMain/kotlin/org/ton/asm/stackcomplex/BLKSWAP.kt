package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class BLKSWAP(
    val i: UByte,
    val j: UByte
) : AsmInstruction {
    override fun toString(): String = "${i + 1u} ${j + 1u} BLKSWAP"

    companion object : TlbConstructorProvider<BLKSWAP> by BLKSWAPTlbConstructor
}

private object BLKSWAPTlbConstructor : TlbConstructor<BLKSWAP>(
    schema = "asm_blkswap#55 i:uint4 j:uint4 = BLKSWAP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BLKSWAP) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): BLKSWAP {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        return BLKSWAP(i, j)
    }
}
