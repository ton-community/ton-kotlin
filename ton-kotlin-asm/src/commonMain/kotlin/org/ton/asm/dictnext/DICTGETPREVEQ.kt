package org.ton.asm.dictnext

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTGETPREVEQ : AsmInstruction, TlbConstructorProvider<DICTGETPREVEQ> by DICTGETPREVEQTlbConstructor {
    override fun toString(): String = "DICTGETPREVEQ"
}

private object DICTGETPREVEQTlbConstructor : TlbConstructor<DICTGETPREVEQ>(
    schema = "asm_dictgetpreveq#f477 = DICTGETPREVEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTGETPREVEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTGETPREVEQ = DICTGETPREVEQ
}
