package org.ton.asm.exception

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THROWANYIFNOT : Instruction, TlbConstructorProvider<THROWANYIFNOT> by THROWANYIFNOTTlbConstructor {
    override fun toString(): String = "THROWANYIFNOT"
}

private object THROWANYIFNOTTlbConstructor : TlbConstructor<THROWANYIFNOT>(
    schema = "asm_throwanyifnot#f2f4 = THROWANYIFNOT;",
    type = THROWANYIFNOT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWANYIFNOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): THROWANYIFNOT = THROWANYIFNOT
}