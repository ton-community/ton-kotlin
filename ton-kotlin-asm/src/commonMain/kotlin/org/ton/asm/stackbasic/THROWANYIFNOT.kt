package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THROWANYIFNOT : AsmInstruction, TlbConstructorProvider<THROWANYIFNOT> by THROWANYIFNOTTlbConstructor {
    override fun toString(): String = "THROWANYIFNOT"
}

private object THROWANYIFNOTTlbConstructor : TlbConstructor<THROWANYIFNOT>(
    schema = "asm_throwanyifnot#f2f4 = THROWANYIFNOT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWANYIFNOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): THROWANYIFNOT = THROWANYIFNOT
}