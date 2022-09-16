package org.ton.asm.appconfig

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MYADDR : AsmInstruction, TlbConstructorProvider<MYADDR> by MYADDRTlbConstructor {
    override fun toString(): String = "MYADDR"
}

private object MYADDRTlbConstructor : TlbConstructor<MYADDR>(
    schema = "asm_myaddr#f828 = MYADDR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MYADDR) {
    }

    override fun loadTlb(cellSlice: CellSlice): MYADDR = MYADDR
}
