package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THIRDQ : AsmInstruction, TlbConstructorProvider<THIRDQ> by THIRDQTlbConstructor {
    override fun toString(): String = "THIRDQ"
}

private object THIRDQTlbConstructor : TlbConstructor<THIRDQ>(
    schema = "asm_thirdq#6f62 = THIRDQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THIRDQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): THIRDQ = THIRDQ
}
