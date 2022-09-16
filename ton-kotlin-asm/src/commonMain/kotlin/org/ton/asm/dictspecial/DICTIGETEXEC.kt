package org.ton.asm.dictspecial

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIGETEXEC : AsmInstruction, TlbConstructorProvider<DICTIGETEXEC> by DICTIGETEXECTlbConstructor {
    override fun toString(): String = "DICTIGETEXEC"
}

private object DICTIGETEXECTlbConstructor : TlbConstructor<DICTIGETEXEC>(
    schema = "asm_dictigetexec#f4a2 = DICTIGETEXEC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIGETEXEC) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIGETEXEC = DICTIGETEXEC
}
