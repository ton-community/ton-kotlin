package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class BLKPUSH(
    val i: UByte,
    val j: UByte,
) : AsmInstruction {
    init {
        require(i >= 1u) { "expected i >= 1, actual $i" }
    }

    override fun toString(): String = "$i $j BLKPUSH"

    companion object : TlbConstructorProvider<BLKPUSH> by BLKPUSHTlbConstructor
}

private object BLKPUSHTlbConstructor : TlbConstructor<BLKPUSH>(
    schema = "asm_blkpush#5f i:(## 4) j:uint4 {1 <= i} = BLKPUSH;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BLKPUSH) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): BLKPUSH {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        return BLKPUSH(i, j)
    }
}