package org.ton.asm.debug

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DUMPSTK : AsmInstruction, TlbConstructorProvider<DUMPSTK> by DUMPSTKTlbConstructor {
    override fun toString(): String = "DUMPSTK"
}

private object DUMPSTKTlbConstructor : TlbConstructor<DUMPSTK>(
    schema = "asm_dumpstk#fe00 = DUMPSTK;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DUMPSTK) {
    }

    override fun loadTlb(cellSlice: CellSlice): DUMPSTK = DUMPSTK
}
