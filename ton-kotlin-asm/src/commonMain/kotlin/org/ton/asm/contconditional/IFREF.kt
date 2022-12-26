package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class IFREF(
    var ref: Cell
) : AsmInstruction {
    override fun toString(): String = "$ref IFREF"

    companion object : TlbConstructorProvider<IFREF> by IFREFTlbConstructor
}

private object IFREFTlbConstructor : TlbConstructor<IFREF>(
    schema = "asm_ifref#e300 c:^Cell = IFREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFREF) {
        cellBuilder.storeRef(value.ref)
    }

    override fun loadTlb(cellSlice: CellSlice): IFREF {
        val ref = cellSlice.loadRef()
        return IFREF(ref)
    }
}
