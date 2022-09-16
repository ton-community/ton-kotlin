package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SWAP : AsmInstruction, TlbConstructorProvider<SWAP> by SWAPTlbConstructor {
    override fun toString(): String = "SWAP"
}

private object SWAPTlbConstructor : TlbConstructor<SWAP>(
    schema = "asm_swap#01 = SWAP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SWAP) {
    }

    override fun loadTlb(cellSlice: CellSlice): SWAP = SWAP
}