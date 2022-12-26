package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class IFREFELSEREF(
    var c1: Cell,
    var c2: Cell
) : AsmInstruction {
    override fun toString(): String = "$c1 $c2 IFREFELSEREF"

    companion object : TlbConstructorProvider<IFREFELSEREF> by IFREFELSEREFTlbConstructor
}

private object IFREFELSEREFTlbConstructor : TlbConstructor<IFREFELSEREF>(
    schema = "asm_ifrefelseref#e30f c1:^Cell c2:^Cell = IFREFELSEREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFREFELSEREF) {
        cellBuilder.storeRef(value.c1)
        cellBuilder.storeRef(value.c2)
    }

    override fun loadTlb(cellSlice: CellSlice): IFREFELSEREF {
        val c1 = cellSlice.loadRef()
        val c2 = cellSlice.loadRef()
        return IFREFELSEREF(c1, c2)
    }
}
