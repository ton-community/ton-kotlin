package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object EXECUTE : AsmInstruction, TlbConstructorProvider<EXECUTE> by EXECUTETlbConstructor {
    override fun toString(): String = "EXECUTE"
}

private object EXECUTETlbConstructor : TlbConstructor<EXECUTE>(
    schema = "asm_execute#d8 = EXECUTE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: EXECUTE) {
    }

    override fun loadTlb(cellSlice: CellSlice): EXECUTE = EXECUTE
}
