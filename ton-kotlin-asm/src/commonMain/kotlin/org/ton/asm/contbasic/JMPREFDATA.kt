package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.cell.Cell
import org.ton.tlb.providers.TlbConstructorProvider

data class JMPREFDATA(
    var ref: Cell
) : AsmInstruction {
    override fun toString(): String = "$ref JMPREFDATA"

    companion object : TlbConstructorProvider<JMPREFDATA> by JMPREFDATATlbConstructor
}

private object JMPREFDATATlbConstructor : TlbConstructor<JMPREFDATA>(
    schema = "asm_jmprefdata#db3e c:^Cell = JMPREFDATA;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: JMPREFDATA) {
        cellBuilder.storeRef(value.ref)
    }

    override fun loadTlb(cellSlice: CellSlice): JMPREFDATA {
        val ref = cellSlice.loadRef()
        return JMPREFDATA(ref)
    }
}
