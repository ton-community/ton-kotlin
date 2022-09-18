package org.ton.asm.constdata

import org.ton.asm.AsmInstruction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHREFSLICE(
    val c: Cell,
) : AsmInstruction {
    override fun toString(): String = "$c PUSHREFSLICE"

    companion object : TlbConstructorProvider<PUSHREFSLICE> by PUSHREFSLICETlbConstructor
}

private object PUSHREFSLICETlbConstructor : TlbConstructor<PUSHREFSLICE>(
    schema = "asm_pushrefslice#89 c:^Cell = PUSHREFSLICE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHREFSLICE) {
        cellBuilder.storeRef(value.c)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHREFSLICE {
        val c = cellSlice.loadRef()
        return PUSHREFSLICE(c)
    }
}