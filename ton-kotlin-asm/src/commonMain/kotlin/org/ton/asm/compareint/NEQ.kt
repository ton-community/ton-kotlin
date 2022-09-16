package org.ton.asm.compareint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NEQ : AsmInstruction, TlbConstructorProvider<NEQ> by NEQTlbConstructor {
    override fun toString(): String = "NEQ"
}

private object NEQTlbConstructor : TlbConstructor<NEQ>(
    schema = "asm_neq#bd = NEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): NEQ = NEQ
}
