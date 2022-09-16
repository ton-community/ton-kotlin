package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THENRET : AsmInstruction, TlbConstructorProvider<THENRET> by THENRETTlbConstructor {
    override fun toString(): String = "THENRET"
}

private object THENRETTlbConstructor : TlbConstructor<THENRET>(
    schema = "asm_thenret#edf6 = THENRET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THENRET) {
    }

    override fun loadTlb(cellSlice: CellSlice): THENRET = THENRET
}