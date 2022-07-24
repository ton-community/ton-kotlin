package org.ton.asm.flow.conditional

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFRET : Instruction, TlbConstructorProvider<IFRET> by IFRETTlbConstructor {
    override fun toString(): String = "IFRET"
}

private object IFRETTlbConstructor : TlbConstructor<IFRET>(
    schema = "asm_ifret#dc = IFRET;",
    type = IFRET::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFRET) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFRET = IFRET
}