package org.ton.asm.contloops

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object REPEATBRK : AsmInstruction, TlbConstructorProvider<REPEATBRK> by REPEATBRKTlbConstructor {
    override fun toString(): String = "REPEATBRK"
}

private object REPEATBRKTlbConstructor : TlbConstructor<REPEATBRK>(
    schema = "asm_repeatbrk#e314 = REPEATBRK;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: REPEATBRK) {
    }

    override fun loadTlb(cellSlice: CellSlice): REPEATBRK = REPEATBRK
}
