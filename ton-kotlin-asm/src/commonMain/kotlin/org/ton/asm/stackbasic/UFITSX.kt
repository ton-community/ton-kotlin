package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UFITSX : AsmInstruction, TlbConstructorProvider<UFITSX> by UFITSXTlbConstructor {
    override fun toString(): String = "UFITSX"
}

private object UFITSXTlbConstructor : TlbConstructor<UFITSX>(
    schema = "asm_ufitsx#b601 = UFITSX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UFITSX) {
    }

    override fun loadTlb(cellSlice: CellSlice): UFITSX = UFITSX
}