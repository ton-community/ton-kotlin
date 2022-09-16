package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THROWANYIF : AsmInstruction, TlbConstructorProvider<THROWANYIF> by THROWANYIFTlbConstructor {
    override fun toString(): String = "THROWANYIF"
}

private object THROWANYIFTlbConstructor : TlbConstructor<THROWANYIF>(
    schema = "asm_throwanyif#f2f2 = THROWANYIF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWANYIF) {
    }

    override fun loadTlb(cellSlice: CellSlice): THROWANYIF = THROWANYIF
}