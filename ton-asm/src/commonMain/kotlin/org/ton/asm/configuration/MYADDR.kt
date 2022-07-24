package org.ton.asm.configuration

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MYADDR : Instruction, TlbConstructorProvider<MYADDR> by MYADDRTlbConstructor {
    override fun toString(): String = "MYADDR"
}

private object MYADDRTlbConstructor : TlbConstructor<MYADDR>(
    schema = "asm_myaddr#fb28 = MYADDR;",
    type = MYADDR::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MYADDR) {
    }

    override fun loadTlb(cellSlice: CellSlice): MYADDR = MYADDR
}