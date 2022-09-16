package org.ton.asm.apprnd

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETRAND : AsmInstruction, TlbConstructorProvider<SETRAND> by SETRANDTlbConstructor {
    override fun toString(): String = "SETRAND"
}

private object SETRANDTlbConstructor : TlbConstructor<SETRAND>(
    schema = "asm_setrand#f814 = SETRAND;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETRAND) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETRAND = SETRAND
}
