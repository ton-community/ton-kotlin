package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDONES : AsmInstruction, TlbConstructorProvider<LDONES> by LDONESTlbConstructor {
    override fun toString(): String = "LDONES"
}

private object LDONESTlbConstructor : TlbConstructor<LDONES>(
    schema = "asm_ldones#d761 = LDONES;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDONES) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDONES = LDONES
}