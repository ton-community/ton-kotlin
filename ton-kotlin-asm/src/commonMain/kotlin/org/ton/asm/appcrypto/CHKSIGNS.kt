package org.ton.asm.appcrypto

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CHKSIGNS : AsmInstruction, TlbConstructorProvider<CHKSIGNS> by CHKSIGNSTlbConstructor {
    override fun toString(): String = "CHKSIGNS"
}

private object CHKSIGNSTlbConstructor : TlbConstructor<CHKSIGNS>(
    schema = "asm_chksigns#f911 = CHKSIGNS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CHKSIGNS) {
    }

    override fun loadTlb(cellSlice: CellSlice): CHKSIGNS = CHKSIGNS
}
