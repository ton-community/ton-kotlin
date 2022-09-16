package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFNOTRET : AsmInstruction, TlbConstructorProvider<IFNOTRET> by IFNOTRETTlbConstructor {
    override fun toString(): String = "IFNOTRET"
}

private object IFNOTRETTlbConstructor : TlbConstructor<IFNOTRET>(
    schema = "asm_ifnotret#dd = IFNOTRET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNOTRET) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFNOTRET = IFNOTRET
}
