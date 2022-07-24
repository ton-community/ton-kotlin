package org.ton.asm.flow.conditional

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CONDSEL : TlbConstructorProvider<CONDSEL> by CONDSELTlbConstructor {
    override fun toString(): String = "CONDSEL"
}

private object CONDSELTlbConstructor : TlbConstructor<CONDSEL>(
    schema = "asm_condsel#e304 = CONDSEL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CONDSEL) {
    }

    override fun loadTlb(cellSlice: CellSlice): CONDSEL = CONDSEL
}