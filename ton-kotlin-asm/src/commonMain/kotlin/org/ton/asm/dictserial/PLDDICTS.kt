package org.ton.asm.dictserial

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDDICTS : AsmInstruction, TlbConstructorProvider<PLDDICTS> by PLDDICTSTlbConstructor {
    override fun toString(): String = "PLDDICTS"
}

private object PLDDICTSTlbConstructor : TlbConstructor<PLDDICTS>(
    schema = "asm_plddicts#f403 = PLDDICTS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDDICTS) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDDICTS = PLDDICTS
}
