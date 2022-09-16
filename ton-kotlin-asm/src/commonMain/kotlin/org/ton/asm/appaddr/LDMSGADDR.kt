package org.ton.asm.appaddr

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDMSGADDR : AsmInstruction, TlbConstructorProvider<LDMSGADDR> by LDMSGADDRTlbConstructor {
    override fun toString(): String = "LDMSGADDR"
}

private object LDMSGADDRTlbConstructor : TlbConstructor<LDMSGADDR>(
    schema = "asm_ldmsgaddr#fa40 = LDMSGADDR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDMSGADDR) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDMSGADDR = LDMSGADDR
}
