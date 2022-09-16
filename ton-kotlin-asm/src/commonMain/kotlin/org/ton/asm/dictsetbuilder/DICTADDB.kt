package org.ton.asm.dictsetbuilder

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTADDB : AsmInstruction, TlbConstructorProvider<DICTADDB> by DICTADDBTlbConstructor {
    override fun toString(): String = "DICTADDB"
}

private object DICTADDBTlbConstructor : TlbConstructor<DICTADDB>(
    schema = "asm_dictaddb#f451 = DICTADDB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTADDB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTADDB = DICTADDB
}
