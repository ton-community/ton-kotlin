package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object TPUSH : AsmInstruction, TlbConstructorProvider<TPUSH> by TPUSHTlbConstructor {
    override fun toString(): String = "TPUSH"
}

private object TPUSHTlbConstructor : TlbConstructor<TPUSH>(
    schema = "asm_tpush#6f8c = TPUSH;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TPUSH) {
    }

    override fun loadTlb(cellSlice: CellSlice): TPUSH = TPUSH
}
