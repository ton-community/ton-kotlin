package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object HASHSU : AsmInstruction, TlbConstructorProvider<HASHSU> by HASHSUTlbConstructor {
    override fun toString(): String = "HASHSU"
}

private object HASHSUTlbConstructor : TlbConstructor<HASHSU>(
    schema = "asm_hashsu#f901 = HASHSU;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: HASHSU) {
    }

    override fun loadTlb(cellSlice: CellSlice): HASHSU = HASHSU
}