package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object FIRSTQ : AsmInstruction, TlbConstructorProvider<FIRSTQ> by FIRSTQTlbConstructor {
    override fun toString(): String = "FIRSTQ"
}

private object FIRSTQTlbConstructor : TlbConstructor<FIRSTQ>(
    schema = "asm_firstq#6f60 = FIRSTQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: FIRSTQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): FIRSTQ = FIRSTQ
}
