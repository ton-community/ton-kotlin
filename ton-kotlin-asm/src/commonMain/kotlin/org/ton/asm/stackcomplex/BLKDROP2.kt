package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class BLKDROP2(
    val i: UByte,
    val j: UByte
) : AsmInstruction {
    init {
        require(i >= 1u) { "expected i >= 1, actual $i" }
    }

    override fun toString(): String = "$i $j BLKDROP2"

    companion object : TlbConstructorProvider<BLKDROP2> by BLKDROP2TlbConstructor
}

private object BLKDROP2TlbConstructor : TlbConstructor<BLKDROP2>(
    schema = "asm_blkdrop2#6c i:(## 4) j:uint4 {1 <= i} = BLKDROP2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BLKDROP2) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): BLKDROP2 {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        return BLKDROP2(i, j)
    }
}
