package org.ton.asm.configuration

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CONFIGDICT : Instruction, TlbConstructorProvider<CONFIGDICT> by CONFIGDICTTlbConstructor {
    override fun toString(): String = "CONFIGDICT"
}

private object CONFIGDICTTlbConstructor : TlbConstructor<CONFIGDICT>(
    schema = "asm_configdict#fb30 = CONFIGDICT;",
    type = CONFIGDICT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CONFIGDICT) {
    }

    override fun loadTlb(cellSlice: CellSlice): CONFIGDICT = CONFIGDICT
}