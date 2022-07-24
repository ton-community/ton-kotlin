package org.ton.asm.stack.basic

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DUP : Instruction, TlbConstructorProvider<DUP> by DUPTlbConstructor {
    override fun toString(): String = "DUP"
}

private object DUPTlbConstructor : TlbConstructor<DUP>(
    schema = "asm_dup#20 = DUP;",
    type = DUP::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DUP) {
    }

    override fun loadTlb(cellSlice: CellSlice): DUP = DUP
}