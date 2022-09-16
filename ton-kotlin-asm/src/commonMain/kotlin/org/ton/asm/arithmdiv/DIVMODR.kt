package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DIVMODR : AsmInstruction, TlbConstructorProvider<DIVMODR> by DIVMODRTlbConstructor {
    override fun toString(): String = "DIVMODR"
}

private object DIVMODRTlbConstructor : TlbConstructor<DIVMODR>(
    schema = "asm_divmodr#a90d = DIVMODR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DIVMODR) {
    }

    override fun loadTlb(cellSlice: CellSlice): DIVMODR = DIVMODR
}
