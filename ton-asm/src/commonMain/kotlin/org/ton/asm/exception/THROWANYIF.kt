package org.ton.asm.exception

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THROWANYIF : Instruction, TlbConstructorProvider<THROWANYIF> by THROWANYIFTlbConstructor {
    override fun toString(): String = "THROWANYIF"
}

private object THROWANYIFTlbConstructor : TlbConstructor<THROWANYIF>(
    schema = "asm_throwanyif#f2f2 = THROWANYIF;",
    type = THROWANYIF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWANYIF) {
    }

    override fun loadTlb(cellSlice: CellSlice): THROWANYIF = THROWANYIF
}