package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RETDATA : AsmInstruction, TlbConstructorProvider<RETDATA> by RETDATATlbConstructor {
    override fun toString(): String = "RETDATA"
}

private object RETDATATlbConstructor : TlbConstructor<RETDATA>(
    schema = "asm_retdata#db3f = RETDATA;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RETDATA) {
    }

    override fun loadTlb(cellSlice: CellSlice): RETDATA = RETDATA
}
