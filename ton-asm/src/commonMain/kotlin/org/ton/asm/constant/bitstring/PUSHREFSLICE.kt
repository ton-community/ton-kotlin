package org.ton.asm.constant.bitstring

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PUSHREFSLICE : Instruction, TlbConstructorProvider<PUSHREFSLICE> by PUSHREFSLICETlbConstructor {
    override fun toString(): String = "PUSHREFSLICE"
}

private object PUSHREFSLICETlbConstructor : TlbConstructor<PUSHREFSLICE>(
    schema = "asm_pushrefslice#89 = PUSHREFSLICE;",
    type = PUSHREFSLICE::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHREFSLICE) {
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHREFSLICE = PUSHREFSLICE
}