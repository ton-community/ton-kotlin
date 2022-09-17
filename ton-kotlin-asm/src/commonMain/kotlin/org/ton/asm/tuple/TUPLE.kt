package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class TUPLE(
    val n: UByte
) : AsmInstruction {
    override fun toString(): String = "$n TUPLE"

    companion object : TlbConstructorProvider<TUPLE> by TUPLETlbConstructor
}

private object TUPLETlbConstructor : TlbConstructor<TUPLE>(
    schema = "asm_tuple#6f0 n:uint4 = TUPLE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TUPLE) {
        cellBuilder.storeUInt(value.n, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): TUPLE {
        val n = cellSlice.loadUInt(4).toUByte()
        return TUPLE(n)
    }
}
