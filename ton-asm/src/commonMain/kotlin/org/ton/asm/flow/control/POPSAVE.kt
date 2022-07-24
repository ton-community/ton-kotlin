package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class POPSAVE(
    val c: Int
) : Instruction {
    override fun toString(): String = "c$c POPSAVE"

    companion object : TlbConstructorProvider<POPSAVE> by POPSAVETlbConstructor
}

private object POPSAVETlbConstructor : TlbConstructor<POPSAVE>(
    schema = "asm_popsave#ed9 c:uint4 = POPSAVE;",
    type = POPSAVE::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: POPSAVE) {
        cellBuilder.storeUInt(value.c, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): POPSAVE {
        val c = cellSlice.loadUInt(4).toInt()
        return POPSAVE(c)
    }
}