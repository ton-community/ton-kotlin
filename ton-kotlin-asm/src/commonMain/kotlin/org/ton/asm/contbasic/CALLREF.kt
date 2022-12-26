package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class CALLREF(
    var ref: Cell
) : AsmInstruction {
    override fun toString(): String = "$ref CALLREF"

    companion object : TlbConstructorProvider<CALLREF> by CALLREFTlbConstructor
}

private object CALLREFTlbConstructor : TlbConstructor<CALLREF>(
    schema = "asm_callref#db3c c:^Cell = CALLREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CALLREF) {
        cellBuilder.storeRef(value.ref)
    }

    override fun loadTlb(cellSlice: CellSlice): CALLREF {
        val ref = cellSlice.loadRef()
        return CALLREF(ref)
    }
}
