package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CALLCCVARARGS : AsmInstruction, TlbConstructorProvider<CALLCCVARARGS> by CALLCCVARARGSTlbConstructor {
    override fun toString(): String = "CALLCCVARARGS"
}

private object CALLCCVARARGSTlbConstructor : TlbConstructor<CALLCCVARARGS>(
    schema = "asm_callccvarargs#db3b = CALLCCVARARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CALLCCVARARGS) {
    }

    override fun loadTlb(cellSlice: CellSlice): CALLCCVARARGS = CALLCCVARARGS
}
