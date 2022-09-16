package org.ton.asm.contloops

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object WHILEBRK : AsmInstruction, TlbConstructorProvider<WHILEBRK> by WHILEBRKTlbConstructor {
    override fun toString(): String = "WHILEBRK"
}

private object WHILEBRKTlbConstructor : TlbConstructor<WHILEBRK>(
    schema = "asm_whilebrk#e318 = WHILEBRK;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: WHILEBRK) {
    }

    override fun loadTlb(cellSlice: CellSlice): WHILEBRK = WHILEBRK
}
