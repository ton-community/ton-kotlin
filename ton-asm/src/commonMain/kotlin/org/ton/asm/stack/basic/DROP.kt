package org.ton.asm.stack.basic

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DROP : Instruction, TlbConstructorProvider<DROP> by DROPTlbConstructor {
    override fun toString(): String = "DROP"
}

private object DROPTlbConstructor : TlbConstructor<DROP>(
    schema = "asm_drop#30 = DROP;",
    type = DROP::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DROP) {
    }

    override fun loadTlb(cellSlice: CellSlice): DROP = DROP
}