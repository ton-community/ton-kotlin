package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

public data class POP(
    val i: Int
) : AsmInstruction {
    override fun toString(): String = "s$i POP"

    public companion object : TlbConstructorProvider<POP> by POPTlbConstructor
}

private object POPTlbConstructor : TlbConstructor<POP>(
    schema = "asm_pop#3 i:uint4 = POP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: POP) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): POP {
        val i = cellSlice.loadUInt(4).toInt()
        return POP(i)
    }
}
