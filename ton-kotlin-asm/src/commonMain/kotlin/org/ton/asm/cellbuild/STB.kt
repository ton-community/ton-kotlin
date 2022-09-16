package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STB : AsmInstruction, TlbConstructorProvider<STB> by STBTlbConstructor {
    override fun toString(): String = "STB"
}

private object STBTlbConstructor : TlbConstructor<STB>(
    schema = "asm_stb#cf13 = STB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STB) {
    }

    override fun loadTlb(cellSlice: CellSlice): STB = STB
}
