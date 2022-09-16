package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STIX : AsmInstruction, TlbConstructorProvider<STIX> by STIXTlbConstructor {
    override fun toString(): String = "STIX"
}

private object STIXTlbConstructor : TlbConstructor<STIX>(
    schema = "asm_stix#cf00 = STIX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STIX) {
    }

    override fun loadTlb(cellSlice: CellSlice): STIX = STIX
}