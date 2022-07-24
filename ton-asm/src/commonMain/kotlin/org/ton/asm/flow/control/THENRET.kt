package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THENRET : Instruction, TlbConstructorProvider<THENRET> by THENRETTlbConstructor {
    override fun toString(): String = "THENRET"
}

private object THENRETTlbConstructor : TlbConstructor<THENRET>(
    schema = "asm_thenret#edf6 = THENRET;",
    type = THENRET::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THENRET) {
    }

    override fun loadTlb(cellSlice: CellSlice): THENRET = THENRET
}