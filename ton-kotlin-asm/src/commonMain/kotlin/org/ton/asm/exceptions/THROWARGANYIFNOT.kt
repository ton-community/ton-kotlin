package org.ton.asm.exceptions

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THROWARGANYIFNOT : AsmInstruction, TlbConstructorProvider<THROWARGANYIFNOT> by THROWARGANYIFNOTTlbConstructor {
    override fun toString(): String = "THROWARGANYIFNOT"
}

private object THROWARGANYIFNOTTlbConstructor : TlbConstructor<THROWARGANYIFNOT>(
    schema = "asm_throwarganyifnot#f2f5 = THROWARGANYIFNOT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWARGANYIFNOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): THROWARGANYIFNOT = THROWARGANYIFNOT
}
