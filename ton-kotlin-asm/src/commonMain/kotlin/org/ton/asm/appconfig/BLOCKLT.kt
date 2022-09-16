package org.ton.asm.appconfig

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BLOCKLT : AsmInstruction, TlbConstructorProvider<BLOCKLT> by BLOCKLTTlbConstructor {
    override fun toString(): String = "BLOCKLT"
}

private object BLOCKLTTlbConstructor : TlbConstructor<BLOCKLT>(
    schema = "asm_blocklt#f824 = BLOCKLT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BLOCKLT) {
    }

    override fun loadTlb(cellSlice: CellSlice): BLOCKLT = BLOCKLT
}
