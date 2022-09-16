package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDATASIZE : AsmInstruction, TlbConstructorProvider<SDATASIZE> by SDATASIZETlbConstructor {
    override fun toString(): String = "SDATASIZE"
}

private object SDATASIZETlbConstructor : TlbConstructor<SDATASIZE>(
    schema = "asm_sdatasize#f943 = SDATASIZE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDATASIZE) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDATASIZE = SDATASIZE
}