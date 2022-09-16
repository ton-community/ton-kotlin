package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ONLYTOPX : AsmInstruction, TlbConstructorProvider<ONLYTOPX> by ONLYTOPXTlbConstructor {
    override fun toString(): String = "ONLYTOPX"
}

private object ONLYTOPXTlbConstructor : TlbConstructor<ONLYTOPX>(
    schema = "asm_onlytopx#6a = ONLYTOPX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ONLYTOPX) {
    }

    override fun loadTlb(cellSlice: CellSlice): ONLYTOPX = ONLYTOPX
}
