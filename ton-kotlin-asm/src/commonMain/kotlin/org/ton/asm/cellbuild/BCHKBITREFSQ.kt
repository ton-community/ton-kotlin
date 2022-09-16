package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BCHKBITREFSQ : AsmInstruction, TlbConstructorProvider<BCHKBITREFSQ> by BCHKBITREFSQTlbConstructor {
    override fun toString(): String = "BCHKBITREFSQ"
}

private object BCHKBITREFSQTlbConstructor : TlbConstructor<BCHKBITREFSQ>(
    schema = "asm_bchkbitrefsq#cf3f = BCHKBITREFSQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BCHKBITREFSQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): BCHKBITREFSQ = BCHKBITREFSQ
}
