package org.ton.asm.dictsetbuilder

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUADDB : AsmInstruction, TlbConstructorProvider<DICTUADDB> by DICTUADDBTlbConstructor {
    override fun toString(): String = "DICTUADDB"
}

private object DICTUADDBTlbConstructor : TlbConstructor<DICTUADDB>(
    schema = "asm_dictuaddb#f453 = DICTUADDB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUADDB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUADDB = DICTUADDB
}
