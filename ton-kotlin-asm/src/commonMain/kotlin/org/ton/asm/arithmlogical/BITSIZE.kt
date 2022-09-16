package org.ton.asm.arithmlogical

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BITSIZE : AsmInstruction, TlbConstructorProvider<BITSIZE> by BITSIZETlbConstructor {
    override fun toString(): String = "BITSIZE"
}

private object BITSIZETlbConstructor : TlbConstructor<BITSIZE>(
    schema = "asm_bitsize#b602 = BITSIZE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BITSIZE) {
    }

    override fun loadTlb(cellSlice: CellSlice): BITSIZE = BITSIZE
}
