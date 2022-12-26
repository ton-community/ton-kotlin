package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class IFJMPREF(
    var ref: Cell
) : AsmInstruction {
    override fun toString(): String = "$ref IFJMPREF"

    companion object : TlbConstructorProvider<IFJMPREF> by IFJMPREFTlbConstructor
}

private object IFJMPREFTlbConstructor : TlbConstructor<IFJMPREF>(
    schema = "asm_ifjmpref#e302 c:^Cell = IFJMPREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFJMPREF) {
        cellBuilder.storeRef(value.ref)
    }

    override fun loadTlb(cellSlice: CellSlice): IFJMPREF {
        val ref = cellSlice.loadRef()
        return IFJMPREF(ref)
    }
}
