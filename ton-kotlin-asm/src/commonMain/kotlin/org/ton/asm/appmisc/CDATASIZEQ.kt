package org.ton.asm.appmisc

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CDATASIZEQ : AsmInstruction, TlbConstructorProvider<CDATASIZEQ> by CDATASIZEQTlbConstructor {
    override fun toString(): String = "CDATASIZEQ"
}

private object CDATASIZEQTlbConstructor : TlbConstructor<CDATASIZEQ>(
    schema = "asm_cdatasizeq#f940 = CDATASIZEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CDATASIZEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): CDATASIZEQ = CDATASIZEQ
}
