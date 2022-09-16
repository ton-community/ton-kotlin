package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ABS : AsmInstruction, TlbConstructorProvider<ABS> by ABSTlbConstructor {
    override fun toString(): String = "ABS"
}

private object ABSTlbConstructor : TlbConstructor<ABS>(
    schema = "asm_abs#b60b = ABS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ABS) {
    }

    override fun loadTlb(cellSlice: CellSlice): ABS = ABS
}