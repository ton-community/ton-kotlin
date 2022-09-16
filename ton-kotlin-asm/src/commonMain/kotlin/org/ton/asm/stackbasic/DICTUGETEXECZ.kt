package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUGETEXECZ : AsmInstruction, TlbConstructorProvider<DICTUGETEXECZ> by DICTUGETEXECZTlbConstructor {
    override fun toString(): String = "DICTUGETEXECZ"
}

private object DICTUGETEXECZTlbConstructor : TlbConstructor<DICTUGETEXECZ>(
    schema = "asm_dictugetexecz#f4bf = DICTUGETEXECZ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUGETEXECZ) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUGETEXECZ = DICTUGETEXECZ
}