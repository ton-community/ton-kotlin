package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class JMPREF(
    var ref: Cell
) : AsmInstruction {
    override fun toString(): String = "$ref JMPREF"

    companion object : TlbConstructorProvider<JMPREF> by JMPREFTlbConstructor
}

private object JMPREFTlbConstructor : TlbConstructor<JMPREF>(
    schema = "asm_jmpref#db3d c:^Cell = JMPREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: JMPREF) {
        cellBuilder.storeRef(value.ref)
    }

    override fun loadTlb(cellSlice: CellSlice): JMPREF {
        val ref = cellSlice.loadRef()
        return JMPREF(ref)
    }
}
