package org.ton.asm.dictsetbuilder

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIADDB : AsmInstruction, TlbConstructorProvider<DICTIADDB> by DICTIADDBTlbConstructor {
    override fun toString(): String = "DICTIADDB"
}

private object DICTIADDBTlbConstructor : TlbConstructor<DICTIADDB>(
    schema = "asm_dictiaddb#f452 = DICTIADDB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIADDB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIADDB = DICTIADDB
}
