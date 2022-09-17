package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class POP(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i POP"

    companion object : TlbConstructorProvider<POP> by POPTlbConstructor
}

private object POPTlbConstructor : TlbConstructor<POP>(
    schema = "asm_pop#3 i:uint4 = POP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: POP) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): POP {
        val i = cellSlice.loadUInt(4).toUByte()
        return POP(i)
    }
}
