package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DIVMOD : AsmInstruction, TlbConstructorProvider<DIVMOD> by DIVMODTlbConstructor {
    override fun toString(): String = "DIVMOD"
}

private object DIVMODTlbConstructor : TlbConstructor<DIVMOD>(
    schema = "asm_divmod#a90c = DIVMOD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DIVMOD) {
    }

    override fun loadTlb(cellSlice: CellSlice): DIVMOD = DIVMOD
}
