package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SPLIT : AsmInstruction, TlbConstructorProvider<SPLIT> by SPLITTlbConstructor {
    override fun toString(): String = "SPLIT"
}

private object SPLITTlbConstructor : TlbConstructor<SPLIT>(
    schema = "asm_split#d736 = SPLIT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SPLIT) {
    }

    override fun loadTlb(cellSlice: CellSlice): SPLIT = SPLIT
}
