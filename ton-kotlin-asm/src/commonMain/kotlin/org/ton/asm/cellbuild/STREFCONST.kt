package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class STREFCONST(
    val c: Cell
) : AsmInstruction {
    override fun toString(): String = "$c STREFCONST"

    companion object : TlbConstructorProvider<STREFCONST> by STREFCONSTTlbConstructor
}

private object STREFCONSTTlbConstructor : TlbConstructor<STREFCONST>(
    schema = "asm_strefconst#cf20 c:^Cell = STREFCONST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STREFCONST) {
        cellBuilder.storeRef(value.c)
    }

    override fun loadTlb(cellSlice: CellSlice): STREFCONST {
        val c = cellSlice.loadRef()
        return STREFCONST(c)
    }
}
