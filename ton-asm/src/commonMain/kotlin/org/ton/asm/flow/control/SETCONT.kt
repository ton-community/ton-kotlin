package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SETCONT(
    val c: Int
) : Instruction {
    override fun toString(): String = "c$c SETCONT"

    companion object : TlbConstructorProvider<SETCONT> by SETCONTTlbConstructor
}

private object SETCONTTlbConstructor : TlbConstructor<SETCONT>(
    schema = "asm_setcont#ed6 c:uint4 = SETCONT;",
    type = SETCONT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETCONT) {
        cellBuilder.storeUInt(value.c, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SETCONT {
        val c = cellSlice.loadUInt(4).toInt()
        return SETCONT(c)
    }
}
