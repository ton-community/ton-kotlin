package org.ton.asm.dictdelete

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUDELGETREF : AsmInstruction, TlbConstructorProvider<DICTUDELGETREF> by DICTUDELGETREFTlbConstructor {
    override fun toString(): String = "DICTUDELGETREF"
}

private object DICTUDELGETREFTlbConstructor : TlbConstructor<DICTUDELGETREF>(
    schema = "asm_dictudelgetref#f467 = DICTUDELGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUDELGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUDELGETREF = DICTUDELGETREF
}
