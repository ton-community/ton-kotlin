package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CDATASIZE : AsmInstruction, TlbConstructorProvider<CDATASIZE> by CDATASIZETlbConstructor {
    override fun toString(): String = "CDATASIZE"
}

private object CDATASIZETlbConstructor : TlbConstructor<CDATASIZE>(
    schema = "asm_cdatasize#f941 = CDATASIZE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CDATASIZE) {
    }

    override fun loadTlb(cellSlice: CellSlice): CDATASIZE = CDATASIZE
}