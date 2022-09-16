package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ISTUPLE : AsmInstruction, TlbConstructorProvider<ISTUPLE> by ISTUPLETlbConstructor {
    override fun toString(): String = "ISTUPLE"
}

private object ISTUPLETlbConstructor : TlbConstructor<ISTUPLE>(
    schema = "asm_istuple#6f8a = ISTUPLE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ISTUPLE) {
    }

    override fun loadTlb(cellSlice: CellSlice): ISTUPLE = ISTUPLE
}
