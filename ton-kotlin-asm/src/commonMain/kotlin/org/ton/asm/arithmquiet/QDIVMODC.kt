package org.ton.asm.arithmquiet

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QDIVMODC : AsmInstruction, TlbConstructorProvider<QDIVMODC> by QDIVMODCTlbConstructor {
    override fun toString(): String = "QDIVMODC"
}

private object QDIVMODCTlbConstructor : TlbConstructor<QDIVMODC>(
    schema = "asm_qdivmodc#b7a90e = QDIVMODC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QDIVMODC) {
    }

    override fun loadTlb(cellSlice: CellSlice): QDIVMODC = QDIVMODC
}
