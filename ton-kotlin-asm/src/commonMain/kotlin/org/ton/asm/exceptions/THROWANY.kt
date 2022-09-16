package org.ton.asm.exceptions

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THROWANY : AsmInstruction, TlbConstructorProvider<THROWANY> by THROWANYTlbConstructor {
    override fun toString(): String = "THROWANY"
}

private object THROWANYTlbConstructor : TlbConstructor<THROWANY>(
    schema = "asm_throwany#f2f0 = THROWANY;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWANY) {
    }

    override fun loadTlb(cellSlice: CellSlice): THROWANY = THROWANY
}
