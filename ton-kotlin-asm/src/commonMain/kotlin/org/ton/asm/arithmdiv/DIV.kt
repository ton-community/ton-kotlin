package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DIV : AsmInstruction, TlbConstructorProvider<DIV> by DIVTlbConstructor {
    override fun toString(): String = "DIV"
}

private object DIVTlbConstructor : TlbConstructor<DIV>(
    schema = "asm_div#a904 = DIV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DIV) {
    }

    override fun loadTlb(cellSlice: CellSlice): DIV = DIV
}
