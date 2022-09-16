package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDDICTQ : AsmInstruction, TlbConstructorProvider<LDDICTQ> by LDDICTQTlbConstructor {
    override fun toString(): String = "LDDICTQ"
}

private object LDDICTQTlbConstructor : TlbConstructor<LDDICTQ>(
    schema = "asm_lddictq#f406 = LDDICTQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDDICTQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDDICTQ = LDDICTQ
}