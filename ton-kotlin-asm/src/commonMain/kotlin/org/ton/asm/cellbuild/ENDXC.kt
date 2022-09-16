package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ENDXC : AsmInstruction, TlbConstructorProvider<ENDXC> by ENDXCTlbConstructor {
    override fun toString(): String = "ENDXC"
}

private object ENDXCTlbConstructor : TlbConstructor<ENDXC>(
    schema = "asm_endxc#cf23 = ENDXC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ENDXC) {
    }

    override fun loadTlb(cellSlice: CellSlice): ENDXC = ENDXC
}
