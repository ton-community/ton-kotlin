package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CONFIGPARAM : AsmInstruction, TlbConstructorProvider<CONFIGPARAM> by CONFIGPARAMTlbConstructor {
    override fun toString(): String = "CONFIGPARAM"
}

private object CONFIGPARAMTlbConstructor : TlbConstructor<CONFIGPARAM>(
    schema = "asm_configparam#f832 = CONFIGPARAM;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CONFIGPARAM) {
    }

    override fun loadTlb(cellSlice: CellSlice): CONFIGPARAM = CONFIGPARAM
}