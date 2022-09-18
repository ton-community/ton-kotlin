package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.cell.Cell
import org.ton.tlb.providers.TlbConstructorProvider

data class IFNOTREF(
    var ref: Cell
) : AsmInstruction {
    override fun toString(): String = "$ref IFNOTREF"

    companion object : TlbConstructorProvider<IFNOTREF> by IFNOTREFTlbConstructor
}

private object IFNOTREFTlbConstructor : TlbConstructor<IFNOTREF>(
    schema = "asm_ifnotref#e301 c:^Cell = IFNOTREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNOTREF) {
        cellBuilder.storeRef(value.ref)
    }

    override fun loadTlb(cellSlice: CellSlice): IFNOTREF {
        val ref = cellSlice.loadRef()
        return IFNOTREF(ref)
    }
}
