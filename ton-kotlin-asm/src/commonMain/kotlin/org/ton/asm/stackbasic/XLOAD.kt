package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object XLOAD : AsmInstruction, TlbConstructorProvider<XLOAD> by XLOADTlbConstructor {
    override fun toString(): String = "XLOAD"
}

private object XLOADTlbConstructor : TlbConstructor<XLOAD>(
    schema = "asm_xload#d73a = XLOAD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XLOAD) {
    }

    override fun loadTlb(cellSlice: CellSlice): XLOAD = XLOAD
}