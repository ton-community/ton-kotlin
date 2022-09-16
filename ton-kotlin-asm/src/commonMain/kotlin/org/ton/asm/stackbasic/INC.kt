package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object INC : AsmInstruction, TlbConstructorProvider<INC> by INCTlbConstructor {
    override fun toString(): String = "INC"
}

private object INCTlbConstructor : TlbConstructor<INC>(
    schema = "asm_inc#a4 = INC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: INC) {
    }

    override fun loadTlb(cellSlice: CellSlice): INC = INC
}