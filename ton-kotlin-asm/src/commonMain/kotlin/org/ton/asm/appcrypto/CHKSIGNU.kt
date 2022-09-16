package org.ton.asm.appcrypto

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CHKSIGNU : AsmInstruction, TlbConstructorProvider<CHKSIGNU> by CHKSIGNUTlbConstructor {
    override fun toString(): String = "CHKSIGNU"
}

private object CHKSIGNUTlbConstructor : TlbConstructor<CHKSIGNU>(
    schema = "asm_chksignu#f910 = CHKSIGNU;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CHKSIGNU) {
    }

    override fun loadTlb(cellSlice: CellSlice): CHKSIGNU = CHKSIGNU
}
