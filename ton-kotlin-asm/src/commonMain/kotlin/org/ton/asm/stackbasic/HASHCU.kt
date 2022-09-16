package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object HASHCU : AsmInstruction, TlbConstructorProvider<HASHCU> by HASHCUTlbConstructor {
    override fun toString(): String = "HASHCU"
}

private object HASHCUTlbConstructor : TlbConstructor<HASHCU>(
    schema = "asm_hashcu#f900 = HASHCU;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: HASHCU) {
    }

    override fun loadTlb(cellSlice: CellSlice): HASHCU = HASHCU
}