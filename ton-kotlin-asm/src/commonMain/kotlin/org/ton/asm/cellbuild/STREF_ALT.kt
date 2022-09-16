package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STREF_ALT : AsmInstruction, TlbConstructorProvider<STREF_ALT> by STREF_ALTTlbConstructor {
    override fun toString(): String = "STREF_ALT"
}

private object STREF_ALTTlbConstructor : TlbConstructor<STREF_ALT>(
    schema = "asm_stref_alt#cf10 = STREF_ALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STREF_ALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): STREF_ALT = STREF_ALT
}
