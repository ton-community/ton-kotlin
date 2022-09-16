package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MULDIVR : AsmInstruction, TlbConstructorProvider<MULDIVR> by MULDIVRTlbConstructor {
    override fun toString(): String = "MULDIVR"
}

private object MULDIVRTlbConstructor : TlbConstructor<MULDIVR>(
    schema = "asm_muldivr#a985 = MULDIVR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MULDIVR) {
    }

    override fun loadTlb(cellSlice: CellSlice): MULDIVR = MULDIVR
}
