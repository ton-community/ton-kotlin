package org.ton.asm.dictnext

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUGETPREVEQ : AsmInstruction, TlbConstructorProvider<DICTUGETPREVEQ> by DICTUGETPREVEQTlbConstructor {
    override fun toString(): String = "DICTUGETPREVEQ"
}

private object DICTUGETPREVEQTlbConstructor : TlbConstructor<DICTUGETPREVEQ>(
    schema = "asm_dictugetpreveq#f47f = DICTUGETPREVEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUGETPREVEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUGETPREVEQ = DICTUGETPREVEQ
}
