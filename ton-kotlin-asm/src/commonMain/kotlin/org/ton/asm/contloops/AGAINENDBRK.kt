package org.ton.asm.contloops

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object AGAINENDBRK : AsmInstruction, TlbConstructorProvider<AGAINENDBRK> by AGAINENDBRKTlbConstructor {
    override fun toString(): String = "AGAINENDBRK"
}

private object AGAINENDBRKTlbConstructor : TlbConstructor<AGAINENDBRK>(
    schema = "asm_againendbrk#e31b = AGAINENDBRK;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AGAINENDBRK) {
    }

    override fun loadTlb(cellSlice: CellSlice): AGAINENDBRK = AGAINENDBRK
}
