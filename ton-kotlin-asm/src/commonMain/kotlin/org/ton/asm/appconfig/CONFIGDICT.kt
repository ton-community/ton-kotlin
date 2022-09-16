package org.ton.asm.appconfig

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CONFIGDICT : AsmInstruction, TlbConstructorProvider<CONFIGDICT> by CONFIGDICTTlbConstructor {
    override fun toString(): String = "CONFIGDICT"
}

private object CONFIGDICTTlbConstructor : TlbConstructor<CONFIGDICT>(
    schema = "asm_configdict#f830 = CONFIGDICT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CONFIGDICT) {
    }

    override fun loadTlb(cellSlice: CellSlice): CONFIGDICT = CONFIGDICT
}
