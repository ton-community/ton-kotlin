package org.ton.asm.constint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ONE : AsmInstruction, TlbConstructorProvider<ONE> by ONETlbConstructor {
    override fun toString(): String = "1 PUSHINT"
}

private object ONETlbConstructor : TlbConstructor<ONE>(
    schema = "asm_one#71 = ONE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ONE) {
    }

    override fun loadTlb(cellSlice: CellSlice): ONE = ONE
}
