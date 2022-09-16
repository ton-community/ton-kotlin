package org.ton.asm.dictserial

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SKIPDICT : AsmInstruction, TlbConstructorProvider<SKIPDICT> by SKIPDICTTlbConstructor {
    override fun toString(): String = "SKIPDICT"
}

private object SKIPDICTTlbConstructor : TlbConstructor<SKIPDICT>(
    schema = "asm_skipdict#f401 = SKIPDICT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SKIPDICT) {
    }

    override fun loadTlb(cellSlice: CellSlice): SKIPDICT = SKIPDICT
}
