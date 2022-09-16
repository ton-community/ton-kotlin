package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object XLOADQ : AsmInstruction, TlbConstructorProvider<XLOADQ> by XLOADQTlbConstructor {
    override fun toString(): String = "XLOADQ"
}

private object XLOADQTlbConstructor : TlbConstructor<XLOADQ>(
    schema = "asm_xloadq#d73b = XLOADQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XLOADQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): XLOADQ = XLOADQ
}
