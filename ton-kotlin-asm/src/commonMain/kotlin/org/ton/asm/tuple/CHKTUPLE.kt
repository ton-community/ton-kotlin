package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CHKTUPLE : AsmInstruction, TlbConstructorProvider<CHKTUPLE> by CHKTUPLETlbConstructor {
    override fun toString(): String = "CHKTUPLE"
}

private object CHKTUPLETlbConstructor : TlbConstructor<CHKTUPLE>(
    schema = "asm_chktuple#6f30 = CHKTUPLE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CHKTUPLE) {
    }

    override fun loadTlb(cellSlice: CellSlice): CHKTUPLE = CHKTUPLE
}
