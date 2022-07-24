package org.ton.asm.arithmetic.basic

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DEC : Instruction, TlbConstructorProvider<DEC> by DECTlbConstructor {
    override fun toString(): String = "DEC"
}

private object DECTlbConstructor : TlbConstructor<DEC>(
    schema = "asm_dec#a5 = DEC;",
    type = DEC::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DEC) {
    }

    override fun loadTlb(cellSlice: CellSlice): DEC = DEC
}