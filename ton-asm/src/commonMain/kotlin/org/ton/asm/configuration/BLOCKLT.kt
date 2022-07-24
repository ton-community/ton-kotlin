package org.ton.asm.configuration

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BLOCKLT : Instruction, TlbConstructorProvider<BLOCKLT> by BLOCKLTTlbConstructor {
    override fun toString(): String = "BLOCKLT"
}

private object BLOCKLTTlbConstructor : TlbConstructor<BLOCKLT>(
    schema = "asm_blocklt#fb24 = BLOCKLT;",
    type = BLOCKLT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BLOCKLT) {
    }

    override fun loadTlb(cellSlice: CellSlice): BLOCKLT = BLOCKLT
}