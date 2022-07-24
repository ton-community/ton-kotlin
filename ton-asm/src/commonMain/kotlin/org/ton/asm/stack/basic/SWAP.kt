package org.ton.asm.stack.basic

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SWAP : Instruction, TlbConstructorProvider<SWAP> by SWAPTlbConstructor {
    override fun toString(): String = "SWAP"
}

private object SWAPTlbConstructor : TlbConstructor<SWAP>(
    schema = "asm_swap#01 = SWAP;",
    type = SWAP::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SWAP) {
    }

    override fun loadTlb(cellSlice: CellSlice): SWAP = SWAP
}