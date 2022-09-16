package org.ton.asm.appaddr

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDMSGADDRQ : AsmInstruction, TlbConstructorProvider<LDMSGADDRQ> by LDMSGADDRQTlbConstructor {
    override fun toString(): String = "LDMSGADDRQ"
}

private object LDMSGADDRQTlbConstructor : TlbConstructor<LDMSGADDRQ>(
    schema = "asm_ldmsgaddrq#fa41 = LDMSGADDRQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDMSGADDRQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDMSGADDRQ = LDMSGADDRQ
}
