package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QDIVMODR : AsmInstruction, TlbConstructorProvider<QDIVMODR> by QDIVMODRTlbConstructor {
    override fun toString(): String = "QDIVMODR"
}

private object QDIVMODRTlbConstructor : TlbConstructor<QDIVMODR>(
    schema = "asm_qdivmodr#b7a90d = QDIVMODR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QDIVMODR) {
    }

    override fun loadTlb(cellSlice: CellSlice): QDIVMODR = QDIVMODR
}