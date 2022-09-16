package org.ton.asm.dictserial

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STDICT : AsmInstruction, TlbConstructorProvider<STDICT> by STDICTTlbConstructor {
    override fun toString(): String = "STDICT"
}

private object STDICTTlbConstructor : TlbConstructor<STDICT>(
    schema = "asm_stdict#f400 = STDICT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STDICT) {
    }

    override fun loadTlb(cellSlice: CellSlice): STDICT = STDICT
}
