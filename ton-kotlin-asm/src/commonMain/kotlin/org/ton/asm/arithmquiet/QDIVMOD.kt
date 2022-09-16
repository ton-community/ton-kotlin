package org.ton.asm.arithmquiet

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QDIVMOD : AsmInstruction, TlbConstructorProvider<QDIVMOD> by QDIVMODTlbConstructor {
    override fun toString(): String = "QDIVMOD"
}

private object QDIVMODTlbConstructor : TlbConstructor<QDIVMOD>(
    schema = "asm_qdivmod#b7a90c = QDIVMOD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QDIVMOD) {
    }

    override fun loadTlb(cellSlice: CellSlice): QDIVMOD = QDIVMOD
}
