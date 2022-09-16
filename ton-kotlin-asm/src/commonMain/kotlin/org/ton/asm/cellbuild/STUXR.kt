package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STUXR : AsmInstruction, TlbConstructorProvider<STUXR> by STUXRTlbConstructor {
    override fun toString(): String = "STUXR"
}

private object STUXRTlbConstructor : TlbConstructor<STUXR>(
    schema = "asm_stuxr#cf03 = STUXR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STUXR) {
    }

    override fun loadTlb(cellSlice: CellSlice): STUXR = STUXR
}
