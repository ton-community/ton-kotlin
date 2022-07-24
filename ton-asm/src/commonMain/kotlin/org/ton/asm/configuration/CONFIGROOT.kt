package org.ton.asm.configuration

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CONFIGROOT : Instruction, TlbConstructorProvider<CONFIGROOT> by CONFIGROOTTlbConstructor {
    override fun toString(): String = "CONFIGROOT"
}

private object CONFIGROOTTlbConstructor : TlbConstructor<CONFIGROOT>(
    schema = "asm_configroot#fb29 = CONFIGROOT;",
    type = CONFIGROOT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CONFIGROOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): CONFIGROOT = CONFIGROOT
}