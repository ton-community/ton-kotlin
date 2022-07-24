package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SPLIT : Instruction, TlbConstructorProvider<SPLIT> by SPLITTlbConstructor {
    override fun toString(): String = "SPLIT"
}

private object SPLITTlbConstructor : TlbConstructor<SPLIT>(
    schema = "asm_split#d736 = SPLIT;",
    type = SPLIT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SPLIT) {
    }

    override fun loadTlb(cellSlice: CellSlice): SPLIT = SPLIT
}