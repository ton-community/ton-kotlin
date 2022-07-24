package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SBITS : Instruction, TlbConstructorProvider<SBITS> by SBITSTlbConstructor {
    override fun toString(): String = "SBITS"
}

private object SBITSTlbConstructor : TlbConstructor<SBITS>(
    schema = "asm_sbits#d749 = SBITS;",
    type = SBITS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SBITS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SBITS = SBITS
}