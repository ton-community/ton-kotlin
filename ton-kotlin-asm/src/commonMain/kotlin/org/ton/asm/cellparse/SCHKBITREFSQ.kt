package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCHKBITREFSQ : AsmInstruction, TlbConstructorProvider<SCHKBITREFSQ> by SCHKBITREFSQTlbConstructor {
    override fun toString(): String = "SCHKBITREFSQ"
}

private object SCHKBITREFSQTlbConstructor : TlbConstructor<SCHKBITREFSQ>(
    schema = "asm_schkbitrefsq#d747 = SCHKBITREFSQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCHKBITREFSQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCHKBITREFSQ = SCHKBITREFSQ
}
