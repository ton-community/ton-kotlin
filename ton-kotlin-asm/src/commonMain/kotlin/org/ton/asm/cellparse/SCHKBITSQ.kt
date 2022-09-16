package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCHKBITSQ : AsmInstruction, TlbConstructorProvider<SCHKBITSQ> by SCHKBITSQTlbConstructor {
    override fun toString(): String = "SCHKBITSQ"
}

private object SCHKBITSQTlbConstructor : TlbConstructor<SCHKBITSQ>(
    schema = "asm_schkbitsq#d745 = SCHKBITSQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCHKBITSQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCHKBITSQ = SCHKBITSQ
}
