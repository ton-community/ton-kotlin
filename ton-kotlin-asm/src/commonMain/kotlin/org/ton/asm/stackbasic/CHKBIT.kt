package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CHKBIT : AsmInstruction, TlbConstructorProvider<CHKBIT> by CHKBITTlbConstructor {
    override fun toString(): String = "CHKBIT"
}

private object CHKBITTlbConstructor : TlbConstructor<CHKBIT>(
    schema = "asm_chkbit#b500 = CHKBIT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CHKBIT) {
    }

    override fun loadTlb(cellSlice: CellSlice): CHKBIT = CHKBIT
}