package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NULLROTRIFNOT : AsmInstruction, TlbConstructorProvider<NULLROTRIFNOT> by NULLROTRIFNOTTlbConstructor {
    override fun toString(): String = "NULLROTRIFNOT"
}

private object NULLROTRIFNOTTlbConstructor : TlbConstructor<NULLROTRIFNOT>(
    schema = "asm_nullrotrifnot#6fa3 = NULLROTRIFNOT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NULLROTRIFNOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): NULLROTRIFNOT = NULLROTRIFNOT
}
