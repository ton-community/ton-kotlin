package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUDELGET : AsmInstruction, TlbConstructorProvider<DICTUDELGET> by DICTUDELGETTlbConstructor {
    override fun toString(): String = "DICTUDELGET"
}

private object DICTUDELGETTlbConstructor : TlbConstructor<DICTUDELGET>(
    schema = "asm_dictudelget#f466 = DICTUDELGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUDELGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUDELGET = DICTUDELGET
}