package org.ton.asm.contloops

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object AGAINBRK : AsmInstruction, TlbConstructorProvider<AGAINBRK> by AGAINBRKTlbConstructor {
    override fun toString(): String = "AGAINBRK"
}

private object AGAINBRKTlbConstructor : TlbConstructor<AGAINBRK>(
    schema = "asm_againbrk#e31a = AGAINBRK;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AGAINBRK) {
    }

    override fun loadTlb(cellSlice: CellSlice): AGAINBRK = AGAINBRK
}
