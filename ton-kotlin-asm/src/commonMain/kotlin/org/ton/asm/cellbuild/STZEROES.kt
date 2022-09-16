package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STZEROES : AsmInstruction, TlbConstructorProvider<STZEROES> by STZEROESTlbConstructor {
    override fun toString(): String = "STZEROES"
}

private object STZEROESTlbConstructor : TlbConstructor<STZEROES>(
    schema = "asm_stzeroes#cf40 = STZEROES;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STZEROES) {
    }

    override fun loadTlb(cellSlice: CellSlice): STZEROES = STZEROES
}
