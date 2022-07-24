package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CHKBIT : Instruction, TlbConstructorProvider<CHKBIT> by CHKBITTlbConstructor {
    override fun toString(): String = "CHKBIT"
}

private object CHKBITTlbConstructor : TlbConstructor<CHKBIT>(
    schema = "asm_chkbit#b500 = CHKBIT;",
    type = CHKBIT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CHKBIT) {
    }

    override fun loadTlb(cellSlice: CellSlice): CHKBIT = CHKBIT
}