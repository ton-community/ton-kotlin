package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NEQ : Instruction, TlbConstructorProvider<NEQ> by NEQTlbConstructor {
    override fun toString(): String = "NEQ"
}

private object NEQTlbConstructor : TlbConstructor<NEQ>(
    schema = "asm_neq#bd = NEQ;",
    type = NEQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): NEQ = NEQ
}