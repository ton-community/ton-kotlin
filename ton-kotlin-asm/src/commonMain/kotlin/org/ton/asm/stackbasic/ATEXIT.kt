package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ATEXIT : AsmInstruction, TlbConstructorProvider<ATEXIT> by ATEXITTlbConstructor {
    override fun toString(): String = "ATEXIT"
}

private object ATEXITTlbConstructor : TlbConstructor<ATEXIT>(
    schema = "asm_atexit#edf3 = ATEXIT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ATEXIT) {
    }

    override fun loadTlb(cellSlice: CellSlice): ATEXIT = ATEXIT
}