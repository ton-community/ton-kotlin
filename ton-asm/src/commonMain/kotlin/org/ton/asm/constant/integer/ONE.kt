package org.ton.asm.constant.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ONE : Instruction, TlbConstructorProvider<ONE> by ONETlbConstructor {
    override fun toString(): String = "ONE"
}

private object ONETlbConstructor : TlbConstructor<ONE>(
    schema = "asm_one#71 = ONE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ONE) {
    }

    override fun loadTlb(cellSlice: CellSlice): ONE = ONE
}