package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IF : AsmInstruction, TlbConstructorProvider<IF> by IFTlbConstructor {
    override fun toString(): String = "IF"
}

private object IFTlbConstructor : TlbConstructor<IF>(
    schema = "asm_if#de = IF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IF) {
    }

    override fun loadTlb(cellSlice: CellSlice): IF = IF
}
