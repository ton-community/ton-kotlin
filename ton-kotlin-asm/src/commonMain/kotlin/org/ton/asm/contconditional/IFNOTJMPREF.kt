package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class IFNOTJMPREF(
    var ref: Cell
) : AsmInstruction {
    override fun toString(): String = "$ref IFNOTJMPREF"

    companion object : TlbConstructorProvider<IFNOTJMPREF> by IFNOTJMPREFTlbConstructor
}

private object IFNOTJMPREFTlbConstructor : TlbConstructor<IFNOTJMPREF>(
    schema = "asm_ifnotjmpref#e303 c:^Cell = IFNOTJMPREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNOTJMPREF) {
        cellBuilder.storeRef(value.ref)
    }

    override fun loadTlb(cellSlice: CellSlice): IFNOTJMPREF {
        val ref = cellSlice.loadRef()
        return IFNOTJMPREF(ref)
    }
}
