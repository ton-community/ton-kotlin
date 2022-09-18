package org.ton.asm.constdata

import org.ton.asm.AsmInstruction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHREFCONT(
    val c: Cell,
) : AsmInstruction {
    override fun toString(): String = "$c PUSHREFCONT"

    companion object : TlbConstructorProvider<PUSHREFCONT> by PUSHREFCONTTlbConstructor
}

private object PUSHREFCONTTlbConstructor : TlbConstructor<PUSHREFCONT>(
    schema = "asm_pushrefcont#8a c:^Cell = PUSHREFCONT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHREFCONT) {
        cellBuilder.storeRef(value.c)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHREFCONT {
        val c = cellSlice.loadRef()
        return PUSHREFCONT(c)
    }
}