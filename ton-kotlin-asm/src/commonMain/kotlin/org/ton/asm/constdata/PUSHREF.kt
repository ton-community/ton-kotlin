package org.ton.asm.constdata

import org.ton.asm.AsmInstruction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHREF(
    val c: Cell,
) : AsmInstruction {
    override fun toString(): String = "$c PUSHREF"

    companion object : TlbConstructorProvider<PUSHREF> by PUSHREFTlbConstructor
}

private object PUSHREFTlbConstructor : TlbConstructor<PUSHREF>(
    schema = "asm_pushref#88 c:^Cell = PUSHREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHREF) {
        cellBuilder.storeRef(value.c)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHREF {
        val c = cellSlice.loadRef()
        return PUSHREF(c)
    }
}