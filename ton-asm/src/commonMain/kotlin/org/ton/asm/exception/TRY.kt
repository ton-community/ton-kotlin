package org.ton.asm.exception

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object TRY : Instruction, TlbConstructorProvider<TRY> by TRYTlbConstructor {
    override fun toString(): String = "TRY"
}

private object TRYTlbConstructor : TlbConstructor<TRY>(
    schema = "asm_try#f2ff = TRY;",
    type = TRY::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TRY) {
    }

    override fun loadTlb(cellSlice: CellSlice): TRY = TRY
}