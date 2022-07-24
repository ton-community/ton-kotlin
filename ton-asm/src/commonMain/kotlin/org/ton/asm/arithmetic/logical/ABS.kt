package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ABS : Instruction, TlbConstructorProvider<ABS> by ABSTLbConstructor {
    override fun toString(): String = "ABS"
}

private object ABSTLbConstructor : TlbConstructor<ABS>(
    schema = "asm_abs#b60b = ABS;",
    type = ABS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ABS) {
    }

    override fun loadTlb(cellSlice: CellSlice): ABS = ABS
}