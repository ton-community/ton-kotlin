package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ISNPOS : AsmInstruction, TlbConstructorProvider<ISNPOS> by ISNPOSTlbConstructor {
    override fun toString(): String = "ISNPOS"
}

private object ISNPOSTlbConstructor : TlbConstructor<ISNPOS>(
    schema = "asm_isnpos#c101 = ISNPOS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ISNPOS) {
    }

    override fun loadTlb(cellSlice: CellSlice): ISNPOS = ISNPOS
}