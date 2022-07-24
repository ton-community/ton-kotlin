package org.ton.asm.constant.bitstring

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PUSHREF : Instruction, TlbConstructorProvider<PUSHREF> by PUSHREFTlbConstructor {
    override fun toString(): String = "PUSHREF"
}

private object PUSHREFTlbConstructor : TlbConstructor<PUSHREF>(
    schema = "asm_pushref#88 = PUSHREF;",
    type = PUSHREF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHREF = PUSHREF
}