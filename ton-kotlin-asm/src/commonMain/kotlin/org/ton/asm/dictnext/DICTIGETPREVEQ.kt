package org.ton.asm.dictnext

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIGETPREVEQ : AsmInstruction, TlbConstructorProvider<DICTIGETPREVEQ> by DICTIGETPREVEQTlbConstructor {
    override fun toString(): String = "DICTIGETPREVEQ"
}

private object DICTIGETPREVEQTlbConstructor : TlbConstructor<DICTIGETPREVEQ>(
    schema = "asm_dictigetpreveq#f47b = DICTIGETPREVEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIGETPREVEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIGETPREVEQ = DICTIGETPREVEQ
}
