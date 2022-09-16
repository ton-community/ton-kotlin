package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STUX : AsmInstruction, TlbConstructorProvider<STUX> by STUXTlbConstructor {
    override fun toString(): String = "STUX"
}

private object STUXTlbConstructor : TlbConstructor<STUX>(
    schema = "asm_stux#cf01 = STUX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STUX) {
    }

    override fun loadTlb(cellSlice: CellSlice): STUX = STUX
}
