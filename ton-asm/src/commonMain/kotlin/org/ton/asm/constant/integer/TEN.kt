package org.ton.asm.constant.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object TEN : Instruction, TlbConstructorProvider<TEN> by TENTlbConstructor {
    override fun toString(): String = "TEN"
}

private object TENTlbConstructor : TlbConstructor<TEN>(
    schema = "asm_ten#7a = TEN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TEN) {
    }

    override fun loadTlb(cellSlice: CellSlice): TEN = TEN
}