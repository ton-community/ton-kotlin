package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCHKBITS : Instruction, TlbConstructorProvider<SCHKBITS> by SCHKBITSTlbConstructor {
    override fun toString(): String = "SCHKBITS"
}

private object SCHKBITSTlbConstructor : TlbConstructor<SCHKBITS>(
    schema = "asm_schkbits#d741 = SCHKBITS;",
    type = SCHKBITS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCHKBITS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCHKBITS = SCHKBITS
}