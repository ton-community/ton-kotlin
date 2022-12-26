package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class EXPLODE(
    val n: UByte
) : AsmInstruction {
    override fun toString(): String = "$n EXPLODE"

    companion object : TlbConstructorProvider<EXPLODE> by EXPLODETlbConstructor
}

private object EXPLODETlbConstructor : TlbConstructor<EXPLODE>(
    schema = "asm_explode#6f4 n:uint4 = EXPLODE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: EXPLODE) {
        cellBuilder.storeUInt(value.n, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): EXPLODE {
        val n = cellSlice.loadUInt(4).toUByte()
        return EXPLODE(n)
    }
}
