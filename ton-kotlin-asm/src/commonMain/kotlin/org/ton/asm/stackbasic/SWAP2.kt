package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SWAP2 : AsmInstruction, TlbConstructorProvider<SWAP2> by SWAP2TlbConstructor {
    override fun toString(): String = "SWAP2"
}

private object SWAP2TlbConstructor : TlbConstructor<SWAP2>(
    schema = "asm_swap2#5a = SWAP2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SWAP2) {
    }

    override fun loadTlb(cellSlice: CellSlice): SWAP2 = SWAP2
}