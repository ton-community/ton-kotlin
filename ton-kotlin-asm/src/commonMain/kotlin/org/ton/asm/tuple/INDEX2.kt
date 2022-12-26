package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class INDEX2(
    val i: UByte,
    val j: UByte
) : AsmInstruction {
    override fun toString(): String = "$i $j INDEX2"

    companion object : TlbConstructorProvider<INDEX2> by INDEX2TlbConstructor
}

private object INDEX2TlbConstructor : TlbConstructor<INDEX2>(
    schema = "asm_index2#6fb i:uint2 j:uint2 = INDEX2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: INDEX2) {
        cellBuilder.storeUInt(value.i, 2)
        cellBuilder.storeUInt(value.j, 2)
    }

    override fun loadTlb(cellSlice: CellSlice): INDEX2 {
        val i = cellSlice.loadTinyInt(2).toUByte()
        val j = cellSlice.loadTinyInt(2).toUByte()
        return INDEX2(i, j)
    }
}
