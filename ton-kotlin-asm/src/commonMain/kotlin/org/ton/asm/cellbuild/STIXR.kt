package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STIXR : AsmInstruction, TlbConstructorProvider<STIXR> by STIXRTlbConstructor {
    override fun toString(): String = "STIXR"
}

private object STIXRTlbConstructor : TlbConstructor<STIXR>(
    schema = "asm_stixr#cf02 = STIXR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STIXR) {
    }

    override fun loadTlb(cellSlice: CellSlice): STIXR = STIXR
}
