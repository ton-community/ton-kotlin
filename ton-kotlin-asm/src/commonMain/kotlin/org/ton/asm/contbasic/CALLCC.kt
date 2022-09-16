package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CALLCC : AsmInstruction, TlbConstructorProvider<CALLCC> by CALLCCTlbConstructor {
    override fun toString(): String = "CALLCC"
}

private object CALLCCTlbConstructor : TlbConstructor<CALLCC>(
    schema = "asm_callcc#db34 = CALLCC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CALLCC) {
    }

    override fun loadTlb(cellSlice: CellSlice): CALLCC = CALLCC
}
