package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NULLROTRIFNOT2 : AsmInstruction, TlbConstructorProvider<NULLROTRIFNOT2> by NULLROTRIFNOT2TlbConstructor {
    override fun toString(): String = "NULLROTRIFNOT2"
}

private object NULLROTRIFNOT2TlbConstructor : TlbConstructor<NULLROTRIFNOT2>(
    schema = "asm_nullrotrifnot2#6fa7 = NULLROTRIFNOT2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NULLROTRIFNOT2) {
    }

    override fun loadTlb(cellSlice: CellSlice): NULLROTRIFNOT2 = NULLROTRIFNOT2
}