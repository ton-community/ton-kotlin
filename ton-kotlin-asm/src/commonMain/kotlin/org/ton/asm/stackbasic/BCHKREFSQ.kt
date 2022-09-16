package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BCHKREFSQ : AsmInstruction, TlbConstructorProvider<BCHKREFSQ> by BCHKREFSQTlbConstructor {
    override fun toString(): String = "BCHKREFSQ"
}

private object BCHKREFSQTlbConstructor : TlbConstructor<BCHKREFSQ>(
    schema = "asm_bchkrefsq#cf3e = BCHKREFSQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BCHKREFSQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): BCHKREFSQ = BCHKREFSQ
}