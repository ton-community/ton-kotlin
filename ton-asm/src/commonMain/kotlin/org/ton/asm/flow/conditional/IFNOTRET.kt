package org.ton.asm.flow.conditional

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFNOTRET : Instruction, TlbConstructorProvider<IFNOTRET> by IFNOTRETTlbConstructor {
    override fun toString(): String = "IFNOTRET"
}

private object IFNOTRETTlbConstructor : TlbConstructor<IFNOTRET>(
    schema = "asm_ifnotret#dd = IFNOTRET;",
    type = IFNOTRET::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNOTRET) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFNOTRET = IFNOTRET
}