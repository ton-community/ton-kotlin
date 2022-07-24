package org.ton.asm.flow.conditional

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IF : Instruction, TlbConstructorProvider<IF> by IFTlbConstructor {
    override fun toString(): String = "IF"
}

private object IFTlbConstructor : TlbConstructor<IF>(
    schema = "asm_if#de = IF;",
    type = IF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IF) {
    }

    override fun loadTlb(cellSlice: CellSlice): IF = IF
}