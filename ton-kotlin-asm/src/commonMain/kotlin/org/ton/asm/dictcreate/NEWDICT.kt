package org.ton.asm.dictcreate

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NEWDICT : AsmInstruction, TlbConstructorProvider<NEWDICT> by NEWDICTTlbConstructor {
    override fun toString(): String = "NEWDICT"
}

private object NEWDICTTlbConstructor : TlbConstructor<NEWDICT>(
    schema = "asm_newdict#6d = NEWDICT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NEWDICT) {
    }

    override fun loadTlb(cellSlice: CellSlice): NEWDICT = NEWDICT
}
