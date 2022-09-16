package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETSECONDQ : AsmInstruction, TlbConstructorProvider<SETSECONDQ> by SETSECONDQTlbConstructor {
    override fun toString(): String = "SETSECONDQ"
}

private object SETSECONDQTlbConstructor : TlbConstructor<SETSECONDQ>(
    schema = "asm_setsecondq#6f71 = SETSECONDQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETSECONDQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETSECONDQ = SETSECONDQ
}