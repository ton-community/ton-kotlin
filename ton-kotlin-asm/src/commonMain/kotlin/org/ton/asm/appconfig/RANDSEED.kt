package org.ton.asm.appconfig

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RANDSEED : AsmInstruction, TlbConstructorProvider<RANDSEED> by RANDSEEDTlbConstructor {
    override fun toString(): String = "RANDSEED"
}

private object RANDSEEDTlbConstructor : TlbConstructor<RANDSEED>(
    schema = "asm_randseed#f826 = RANDSEED;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RANDSEED) {
    }

    override fun loadTlb(cellSlice: CellSlice): RANDSEED = RANDSEED
}
