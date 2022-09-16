package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETTHIRDQ : AsmInstruction, TlbConstructorProvider<SETTHIRDQ> by SETTHIRDQTlbConstructor {
    override fun toString(): String = "SETTHIRDQ"
}

private object SETTHIRDQTlbConstructor : TlbConstructor<SETTHIRDQ>(
    schema = "asm_setthirdq#6f72 = SETTHIRDQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETTHIRDQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETTHIRDQ = SETTHIRDQ
}