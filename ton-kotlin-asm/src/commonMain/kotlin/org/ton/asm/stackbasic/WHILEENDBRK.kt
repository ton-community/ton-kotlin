package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object WHILEENDBRK : AsmInstruction, TlbConstructorProvider<WHILEENDBRK> by WHILEENDBRKTlbConstructor {
    override fun toString(): String = "WHILEENDBRK"
}

private object WHILEENDBRKTlbConstructor : TlbConstructor<WHILEENDBRK>(
    schema = "asm_whileendbrk#e319 = WHILEENDBRK;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: WHILEENDBRK) {
    }

    override fun loadTlb(cellSlice: CellSlice): WHILEENDBRK = WHILEENDBRK
}