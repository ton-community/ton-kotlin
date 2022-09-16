package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFNOT : AsmInstruction, TlbConstructorProvider<IFNOT> by IFNOTTlbConstructor {
    override fun toString(): String = "IFNOT"
}

private object IFNOTTlbConstructor : TlbConstructor<IFNOT>(
    schema = "asm_ifnot#df = IFNOT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFNOT = IFNOT
}
