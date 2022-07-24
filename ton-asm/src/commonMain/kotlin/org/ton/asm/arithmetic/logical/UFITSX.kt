package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UFITSX : Instruction, TlbConstructorProvider<UFITSX> by UFITSXTlbConstructor {
    override fun toString(): String = "UFITSX"
}

private object UFITSXTlbConstructor : TlbConstructor<UFITSX>(
    schema = "asm_ufitsx#b601 = UFITSX;",
    type = UFITSX::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UFITSX) {
    }

    override fun loadTlb(cellSlice: CellSlice): UFITSX = UFITSX
}