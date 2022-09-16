package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DIVMODC : AsmInstruction, TlbConstructorProvider<DIVMODC> by DIVMODCTlbConstructor {
    override fun toString(): String = "DIVMODC"
}

private object DIVMODCTlbConstructor : TlbConstructor<DIVMODC>(
    schema = "asm_divmodc#a90e = DIVMODC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DIVMODC) {
    }

    override fun loadTlb(cellSlice: CellSlice): DIVMODC = DIVMODC
}