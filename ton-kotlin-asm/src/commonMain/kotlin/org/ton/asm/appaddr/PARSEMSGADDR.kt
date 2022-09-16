package org.ton.asm.appaddr

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PARSEMSGADDR : AsmInstruction, TlbConstructorProvider<PARSEMSGADDR> by PARSEMSGADDRTlbConstructor {
    override fun toString(): String = "PARSEMSGADDR"
}

private object PARSEMSGADDRTlbConstructor : TlbConstructor<PARSEMSGADDR>(
    schema = "asm_parsemsgaddr#fa42 = PARSEMSGADDR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PARSEMSGADDR) {
    }

    override fun loadTlb(cellSlice: CellSlice): PARSEMSGADDR = PARSEMSGADDR
}
