package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETFIRSTQ : AsmInstruction, TlbConstructorProvider<SETFIRSTQ> by SETFIRSTQTlbConstructor {
    override fun toString(): String = "SETFIRSTQ"
}

private object SETFIRSTQTlbConstructor : TlbConstructor<SETFIRSTQ>(
    schema = "asm_setfirstq#6f70 = SETFIRSTQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETFIRSTQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETFIRSTQ = SETFIRSTQ
}
