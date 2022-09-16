package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTREMMIN : AsmInstruction, TlbConstructorProvider<DICTREMMIN> by DICTREMMINTlbConstructor {
    override fun toString(): String = "DICTREMMIN"
}

private object DICTREMMINTlbConstructor : TlbConstructor<DICTREMMIN>(
    schema = "asm_dictremmin#f492 = DICTREMMIN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTREMMIN) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTREMMIN = DICTREMMIN
}