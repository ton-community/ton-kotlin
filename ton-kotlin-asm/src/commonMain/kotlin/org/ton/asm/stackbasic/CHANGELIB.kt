package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CHANGELIB : AsmInstruction, TlbConstructorProvider<CHANGELIB> by CHANGELIBTlbConstructor {
    override fun toString(): String = "CHANGELIB"
}

private object CHANGELIBTlbConstructor : TlbConstructor<CHANGELIB>(
    schema = "asm_changelib#fb07 = CHANGELIB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CHANGELIB) {
    }

    override fun loadTlb(cellSlice: CellSlice): CHANGELIB = CHANGELIB
}