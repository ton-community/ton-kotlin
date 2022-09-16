package org.ton.asm.arithmlogical

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object AND : AsmInstruction, TlbConstructorProvider<AND> by ANDTlbConstructor {
    override fun toString(): String = "AND"
}

private object ANDTlbConstructor : TlbConstructor<AND>(
    schema = "asm_and#b0 = AND;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AND) {
    }

    override fun loadTlb(cellSlice: CellSlice): AND = AND
}
