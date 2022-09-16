package org.ton.asm.dictserial

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDDICTQ : AsmInstruction, TlbConstructorProvider<PLDDICTQ> by PLDDICTQTlbConstructor {
    override fun toString(): String = "PLDDICTQ"
}

private object PLDDICTQTlbConstructor : TlbConstructor<PLDDICTQ>(
    schema = "asm_plddictq#f407 = PLDDICTQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDDICTQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDDICTQ = PLDDICTQ
}
