package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.cell.Cell
import org.ton.tlb.providers.TlbConstructorProvider

data class IFREFELSE(
    var ref: Cell
) : AsmInstruction {
    override fun toString(): String = "$ref IFREFELSE"

    companion object : TlbConstructorProvider<IFREFELSE> by IFREFELSETlbConstructor
}

private object IFREFELSETlbConstructor : TlbConstructor<IFREFELSE>(
    schema = "asm_ifrefelse#e30d c:^Cell = IFREFELSE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFREFELSE) {
        cellBuilder.storeRef(value.ref)
    }

    override fun loadTlb(cellSlice: CellSlice): IFREFELSE {
        val ref = cellSlice.loadRef()
        return IFREFELSE(ref)
    }
}
