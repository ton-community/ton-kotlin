package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object REWRITEVARADDRQ : AsmInstruction, TlbConstructorProvider<REWRITEVARADDRQ> by REWRITEVARADDRQTlbConstructor {
    override fun toString(): String = "REWRITEVARADDRQ"
}

private object REWRITEVARADDRQTlbConstructor : TlbConstructor<REWRITEVARADDRQ>(
    schema = "asm_rewritevaraddrq#fa47 = REWRITEVARADDRQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: REWRITEVARADDRQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): REWRITEVARADDRQ = REWRITEVARADDRQ
}