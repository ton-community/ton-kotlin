package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SEMPTY : AsmInstruction, TlbConstructorProvider<SEMPTY> by SEMPTYTlbConstructor {
    override fun toString(): String = "SEMPTY"
}

private object SEMPTYTlbConstructor : TlbConstructor<SEMPTY>(
    schema = "asm_sempty#c700 = SEMPTY;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SEMPTY) {
    }

    override fun loadTlb(cellSlice: CellSlice): SEMPTY = SEMPTY
}