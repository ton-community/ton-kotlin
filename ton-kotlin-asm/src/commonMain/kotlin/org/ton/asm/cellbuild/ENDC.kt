package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ENDC : AsmInstruction, TlbConstructorProvider<ENDC> by ENDCTlbConstructor {
    override fun toString(): String = "ENDC"
}

private object ENDCTlbConstructor : TlbConstructor<ENDC>(
    schema = "asm_endc#c9 = ENDC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ENDC) {
    }

    override fun loadTlb(cellSlice: CellSlice): ENDC = ENDC
}
