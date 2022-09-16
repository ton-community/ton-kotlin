package org.ton.asm.dictserial

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDDICT : AsmInstruction, TlbConstructorProvider<LDDICT> by LDDICTTlbConstructor {
    override fun toString(): String = "LDDICT"
}

private object LDDICTTlbConstructor : TlbConstructor<LDDICT>(
    schema = "asm_lddict#f404 = LDDICT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDDICT) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDDICT = LDDICT
}
