package org.ton.asm.appconfig

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CONFIGROOT : AsmInstruction, TlbConstructorProvider<CONFIGROOT> by CONFIGROOTTlbConstructor {
    override fun toString(): String = "CONFIGROOT"
}

private object CONFIGROOTTlbConstructor : TlbConstructor<CONFIGROOT>(
    schema = "asm_configroot#f829 = CONFIGROOT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CONFIGROOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): CONFIGROOT = CONFIGROOT
}
