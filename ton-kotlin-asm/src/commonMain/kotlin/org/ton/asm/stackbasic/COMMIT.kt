package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object COMMIT : AsmInstruction, TlbConstructorProvider<COMMIT> by COMMITTlbConstructor {
    override fun toString(): String = "COMMIT"
}

private object COMMITTlbConstructor : TlbConstructor<COMMIT>(
    schema = "asm_commit#f80f = COMMIT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: COMMIT) {
    }

    override fun loadTlb(cellSlice: CellSlice): COMMIT = COMMIT
}