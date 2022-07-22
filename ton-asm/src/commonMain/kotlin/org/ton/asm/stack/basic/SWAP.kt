package org.ton.asm.stack.basic

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

object SWAP : TlbCodec<SWAP> by SWAPTlbConstructor {
    fun tlbConstructor(): TlbConstructor<SWAP> = SWAPTlbConstructor
    override fun toString(): String = "SWAP"
}

private object SWAPTlbConstructor : TlbConstructor<SWAP>(
    schema = "asm_swap#01 = SWAP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SWAP) {
    }

    override fun loadTlb(cellSlice: CellSlice): SWAP = SWAP
}