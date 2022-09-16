package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDIXQ : AsmInstruction, TlbConstructorProvider<PLDIXQ> by PLDIXQTlbConstructor {
    override fun toString(): String = "PLDIXQ"
}

private object PLDIXQTlbConstructor : TlbConstructor<PLDIXQ>(
    schema = "asm_pldixq#d706 = PLDIXQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDIXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDIXQ = PLDIXQ
}
