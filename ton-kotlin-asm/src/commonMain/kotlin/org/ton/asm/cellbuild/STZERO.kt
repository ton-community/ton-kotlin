package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STZERO : AsmInstruction, TlbConstructorProvider<STZERO> by STZEROTlbConstructor {
    override fun toString(): String = "STZERO"
}

private object STZEROTlbConstructor : TlbConstructor<STZERO>(
    schema = "asm_stzero#cf81 = STZERO;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STZERO) {
    }

    override fun loadTlb(cellSlice: CellSlice): STZERO = STZERO
}
