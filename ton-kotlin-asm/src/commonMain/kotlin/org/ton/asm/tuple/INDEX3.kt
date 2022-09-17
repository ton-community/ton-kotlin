package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class INDEX3(
    val i: UByte,
    val j: UByte,
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "$i $j $k INDEX3"

    companion object : TlbConstructorProvider<INDEX3> by INDEX3TlbConstructor
}

private object INDEX3TlbConstructor : TlbConstructor<INDEX3>(
    schema = "asm_index3#6fe_ i:uint2 j:uint2 k:uint2 = INDEX3;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: INDEX3) {
        cellBuilder.storeUInt(value.i, 2)
        cellBuilder.storeUInt(value.j, 2)
        cellBuilder.storeUInt(value.k, 2)
    }

    override fun loadTlb(cellSlice: CellSlice): INDEX3 {
        val i = cellSlice.loadUInt(2).toUByte()
        val j = cellSlice.loadUInt(2).toUByte()
        val k = cellSlice.loadUInt(2).toUByte()
        return INDEX3(i, j, k)
    }
}
