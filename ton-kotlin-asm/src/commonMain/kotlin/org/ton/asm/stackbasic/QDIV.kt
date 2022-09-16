package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QDIV : AsmInstruction, TlbConstructorProvider<QDIV> by QDIVTlbConstructor {
    override fun toString(): String = "QDIV"
}

private object QDIVTlbConstructor : TlbConstructor<QDIV>(
    schema = "asm_qdiv#b7a904 = QDIV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QDIV) {
    }

    override fun loadTlb(cellSlice: CellSlice): QDIV = QDIV
}