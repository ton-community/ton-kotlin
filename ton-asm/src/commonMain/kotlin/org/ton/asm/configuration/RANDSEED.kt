package org.ton.asm.configuration

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RANDSEED : Instruction, TlbConstructorProvider<RANDSEED> by RANDSEEDTlbConstructor {
    override fun toString(): String = "RANDSEED"
}

private object RANDSEEDTlbConstructor : TlbConstructor<RANDSEED>(
    schema = "asm_randseed#fb26 = RANDSEED;",
    type = RANDSEED::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RANDSEED) {
    }

    override fun loadTlb(cellSlice: CellSlice): RANDSEED = RANDSEED
}