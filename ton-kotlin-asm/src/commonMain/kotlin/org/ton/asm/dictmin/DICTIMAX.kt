package org.ton.asm.dictmin

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIMAX : AsmInstruction, TlbConstructorProvider<DICTIMAX> by DICTIMAXTlbConstructor {
    override fun toString(): String = "DICTIMAX"
}

private object DICTIMAXTlbConstructor : TlbConstructor<DICTIMAX>(
    schema = "asm_dictimax#f48c = DICTIMAX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIMAX) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIMAX = DICTIMAX
}
