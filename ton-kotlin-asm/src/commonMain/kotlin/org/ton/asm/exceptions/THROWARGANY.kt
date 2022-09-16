package org.ton.asm.exceptions

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THROWARGANY : AsmInstruction, TlbConstructorProvider<THROWARGANY> by THROWARGANYTlbConstructor {
    override fun toString(): String = "THROWARGANY"
}

private object THROWARGANYTlbConstructor : TlbConstructor<THROWARGANY>(
    schema = "asm_throwargany#f2f1 = THROWARGANY;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWARGANY) {
    }

    override fun loadTlb(cellSlice: CellSlice): THROWARGANY = THROWARGANY
}
