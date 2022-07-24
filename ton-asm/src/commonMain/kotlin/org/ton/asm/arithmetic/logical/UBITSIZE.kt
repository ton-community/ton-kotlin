package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UBITSIZE : Instruction, TlbConstructorProvider<UBITSIZE> by UBITSIZETlbConstructor {
    override fun toString(): String = "UBITSIZE"
}

private object UBITSIZETlbConstructor : TlbConstructor<UBITSIZE>(
    schema = "asm_ubitsize#b603 = UBITSIZE;",
    type = UBITSIZE::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UBITSIZE) {
    }

    override fun loadTlb(cellSlice: CellSlice): UBITSIZE = UBITSIZE
}