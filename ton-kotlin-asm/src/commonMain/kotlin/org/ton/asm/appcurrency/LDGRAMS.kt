package org.ton.asm.appcurrency

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDGRAMS : AsmInstruction, TlbConstructorProvider<LDGRAMS> by LDGRAMSTlbConstructor {
    override fun toString(): String = "LDGRAMS"
}

private object LDGRAMSTlbConstructor : TlbConstructor<LDGRAMS>(
    schema = "asm_ldgrams#fa00 = LDGRAMS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDGRAMS) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDGRAMS = LDGRAMS
}
