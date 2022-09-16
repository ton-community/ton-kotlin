package org.ton.asm.appgas

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ACCEPT : AsmInstruction, TlbConstructorProvider<ACCEPT> by ACCEPTTlbConstructor {
    override fun toString(): String = "ACCEPT"
}

private object ACCEPTTlbConstructor : TlbConstructor<ACCEPT>(
    schema = "asm_accept#f800 = ACCEPT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ACCEPT) {
    }

    override fun loadTlb(cellSlice: CellSlice): ACCEPT = ACCEPT
}
