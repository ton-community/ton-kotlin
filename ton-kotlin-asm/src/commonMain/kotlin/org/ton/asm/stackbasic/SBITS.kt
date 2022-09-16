package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SBITS : AsmInstruction, TlbConstructorProvider<SBITS> by SBITSTlbConstructor {
    override fun toString(): String = "SBITS"
}

private object SBITSTlbConstructor : TlbConstructor<SBITS>(
    schema = "asm_sbits#d749 = SBITS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SBITS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SBITS = SBITS
}