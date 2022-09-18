package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.cell.Cell
import org.ton.tlb.providers.TlbConstructorProvider

data class IFELSEREF(
    var ref: Cell
) : AsmInstruction {
    override fun toString(): String = "$ref IFELSEREF"

    companion object : TlbConstructorProvider<IFELSEREF> by IFELSEREFTlbConstructor
}

private object IFELSEREFTlbConstructor : TlbConstructor<IFELSEREF>(
    schema = "asm_ifelseref#e30e c:^Cell = IFELSEREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFELSEREF) {
        cellBuilder.storeRef(value.ref)
    }

    override fun loadTlb(cellSlice: CellSlice): IFELSEREF {
        val ref = cellSlice.loadRef()
        return IFELSEREF(ref)
    }
}
