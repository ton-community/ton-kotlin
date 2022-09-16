package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MULDIVMOD : AsmInstruction, TlbConstructorProvider<MULDIVMOD> by MULDIVMODTlbConstructor {
    override fun toString(): String = "MULDIVMOD"
}

private object MULDIVMODTlbConstructor : TlbConstructor<MULDIVMOD>(
    schema = "asm_muldivmod#a98c = MULDIVMOD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MULDIVMOD) {
    }

    override fun loadTlb(cellSlice: CellSlice): MULDIVMOD = MULDIVMOD
}
