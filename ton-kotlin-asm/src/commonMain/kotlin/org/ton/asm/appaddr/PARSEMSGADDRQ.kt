package org.ton.asm.appaddr

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PARSEMSGADDRQ : AsmInstruction, TlbConstructorProvider<PARSEMSGADDRQ> by PARSEMSGADDRQTlbConstructor {
    override fun toString(): String = "PARSEMSGADDRQ"
}

private object PARSEMSGADDRQTlbConstructor : TlbConstructor<PARSEMSGADDRQ>(
    schema = "asm_parsemsgaddrq#fa43 = PARSEMSGADDRQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PARSEMSGADDRQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): PARSEMSGADDRQ = PARSEMSGADDRQ
}
