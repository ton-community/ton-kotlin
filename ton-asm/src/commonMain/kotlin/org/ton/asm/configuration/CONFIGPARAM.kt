package org.ton.asm.configuration

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CONFIGPARAM : Instruction, TlbConstructorProvider<CONFIGPARAM> by CONFIGPARAMTlbConstructor {
    override fun toString(): String = "CONFIGPARAM"
}

private object CONFIGPARAMTlbConstructor : TlbConstructor<CONFIGPARAM>(
    schema = "asm_configparam#fb32 = CONFIGPARAM;",
    type = CONFIGPARAM::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CONFIGPARAM) {
    }

    override fun loadTlb(cellSlice: CellSlice): CONFIGPARAM = CONFIGPARAM
}