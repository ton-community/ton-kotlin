package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDSUBSTR : Instruction, TlbConstructorProvider<SDSUBSTR> by SDSUBSTRTlbConstructor {
    override fun toString(): String = "SDSUBSTR"
}

private object SDSUBSTRTlbConstructor : TlbConstructor<SDSUBSTR>(
    schema = "asm_sdsubstr#d724 = SDSUBSTR;",
    type = SDSUBSTR::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDSUBSTR) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDSUBSTR = SDSUBSTR
}