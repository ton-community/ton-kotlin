package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QDIVR : AsmInstruction, TlbConstructorProvider<QDIVR> by QDIVRTlbConstructor {
    override fun toString(): String = "QDIVR"
}

private object QDIVRTlbConstructor : TlbConstructor<QDIVR>(
    schema = "asm_qdivr#b7a905 = QDIVR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QDIVR) {
    }

    override fun loadTlb(cellSlice: CellSlice): QDIVR = QDIVR
}