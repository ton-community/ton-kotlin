package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object TRY : AsmInstruction, TlbConstructorProvider<TRY> by TRYTlbConstructor {
    override fun toString(): String = "TRY"
}

private object TRYTlbConstructor : TlbConstructor<TRY>(
    schema = "asm_try#f2ff = TRY;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TRY) {
    }

    override fun loadTlb(cellSlice: CellSlice): TRY = TRY
}