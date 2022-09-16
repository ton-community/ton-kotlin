package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STSLICE_ALT : AsmInstruction, TlbConstructorProvider<STSLICE_ALT> by STSLICE_ALTTlbConstructor {
    override fun toString(): String = "STSLICE_ALT"
}

private object STSLICE_ALTTlbConstructor : TlbConstructor<STSLICE_ALT>(
    schema = "asm_stslice_alt#cf12 = STSLICE_ALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STSLICE_ALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): STSLICE_ALT = STSLICE_ALT
}