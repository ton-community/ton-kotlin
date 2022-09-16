package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DIVR : AsmInstruction, TlbConstructorProvider<DIVR> by DIVRTlbConstructor {
    override fun toString(): String = "DIVR"
}

private object DIVRTlbConstructor : TlbConstructor<DIVR>(
    schema = "asm_divr#a905 = DIVR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DIVR) {
    }

    override fun loadTlb(cellSlice: CellSlice): DIVR = DIVR
}
