package org.ton.asm.arithmlogical

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UBITSIZE : AsmInstruction, TlbConstructorProvider<UBITSIZE> by UBITSIZETlbConstructor {
    override fun toString(): String = "UBITSIZE"
}

private object UBITSIZETlbConstructor : TlbConstructor<UBITSIZE>(
    schema = "asm_ubitsize#b603 = UBITSIZE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UBITSIZE) {
    }

    override fun loadTlb(cellSlice: CellSlice): UBITSIZE = UBITSIZE
}
