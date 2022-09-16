package org.ton.asm.dictsub

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SUBDICTUGET : AsmInstruction, TlbConstructorProvider<SUBDICTUGET> by SUBDICTUGETTlbConstructor {
    override fun toString(): String = "SUBDICTUGET"
}

private object SUBDICTUGETTlbConstructor : TlbConstructor<SUBDICTUGET>(
    schema = "asm_subdictuget#f4b3 = SUBDICTUGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SUBDICTUGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): SUBDICTUGET = SUBDICTUGET
}
