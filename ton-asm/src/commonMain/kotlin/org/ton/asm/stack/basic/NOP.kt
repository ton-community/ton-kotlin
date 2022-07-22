package org.ton.asm.stack.basic

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

object NOP : TlbCodec<NOP> by NOPTlbConstructor {
    fun tlbConstructor(): TlbConstructor<NOP> = NOPTlbConstructor
    override fun toString(): String = "NOP"
}

private object NOPTlbConstructor : TlbConstructor<NOP>(
    schema = "asm_nop#00 = NOP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NOP) {
    }

    override fun loadTlb(cellSlice: CellSlice): NOP = NOP
}