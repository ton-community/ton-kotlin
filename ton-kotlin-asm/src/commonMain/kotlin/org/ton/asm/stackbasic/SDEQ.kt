package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDEQ : AsmInstruction, TlbConstructorProvider<SDEQ> by SDEQTlbConstructor {
    override fun toString(): String = "SDEQ"
}

private object SDEQTlbConstructor : TlbConstructor<SDEQ>(
    schema = "asm_sdeq#c705 = SDEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDEQ = SDEQ
}