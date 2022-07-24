package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ATEXIT : Instruction, TlbConstructorProvider<ATEXIT> by ATEXITTlbConstructor {
    override fun toString(): String = "ATEXIT"
}

private object ATEXITTlbConstructor : TlbConstructor<ATEXIT>(
    schema = "asm_atexit#edf3 = ATEXIT;",
    type = ATEXIT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ATEXIT) {
    }

    override fun loadTlb(cellSlice: CellSlice): ATEXIT = ATEXIT
}