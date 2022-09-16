package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STBREFR_ALT : AsmInstruction, TlbConstructorProvider<STBREFR_ALT> by STBREFR_ALTTlbConstructor {
    override fun toString(): String = "STBREFR_ALT"
}

private object STBREFR_ALTTlbConstructor : TlbConstructor<STBREFR_ALT>(
    schema = "asm_stbrefr_alt#cf15 = STBREFR_ALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STBREFR_ALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): STBREFR_ALT = STBREFR_ALT
}