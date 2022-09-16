package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BLKSWX : AsmInstruction, TlbConstructorProvider<BLKSWX> by BLKSWXTlbConstructor {
    override fun toString(): String = "BLKSWX"
}

private object BLKSWXTlbConstructor : TlbConstructor<BLKSWX>(
    schema = "asm_blkswx#63 = BLKSWX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BLKSWX) {
    }

    override fun loadTlb(cellSlice: CellSlice): BLKSWX = BLKSWX
}
