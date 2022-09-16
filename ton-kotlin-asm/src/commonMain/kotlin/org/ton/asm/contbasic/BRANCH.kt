package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BRANCH : AsmInstruction, TlbConstructorProvider<BRANCH> by BRANCHTlbConstructor {
    override fun toString(): String = "BRANCH"
}

private object BRANCHTlbConstructor : TlbConstructor<BRANCH>(
    schema = "asm_branch#db32 = BRANCH;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BRANCH) {
    }

    override fun loadTlb(cellSlice: CellSlice): BRANCH = BRANCH
}
