package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFRET : AsmInstruction, TlbConstructorProvider<IFRET> by IFRETTlbConstructor {
    override fun toString(): String = "IFRET"
}

private object IFRETTlbConstructor : TlbConstructor<IFRET>(
    schema = "asm_ifret#dc = IFRET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFRET) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFRET = IFRET
}
