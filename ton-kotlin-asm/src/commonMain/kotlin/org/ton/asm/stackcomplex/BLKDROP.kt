package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class BLKDROP(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "$i BLKDROP"

    companion object : TlbConstructorProvider<BLKDROP> by BLKDROPTlbConstructor
}

private object BLKDROPTlbConstructor : TlbConstructor<BLKDROP>(
    schema = "asm_blkdrop#5f0 i:uint4 = BLKDROP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BLKDROP) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): BLKDROP {
        val i = cellSlice.loadUInt(4).toUByte()
        return BLKDROP(i)
    }
}
