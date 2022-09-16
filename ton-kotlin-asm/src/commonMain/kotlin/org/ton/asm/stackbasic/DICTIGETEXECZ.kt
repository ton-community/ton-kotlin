package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIGETEXECZ : AsmInstruction, TlbConstructorProvider<DICTIGETEXECZ> by DICTIGETEXECZTlbConstructor {
    override fun toString(): String = "DICTIGETEXECZ"
}

private object DICTIGETEXECZTlbConstructor : TlbConstructor<DICTIGETEXECZ>(
    schema = "asm_dictigetexecz#f4be = DICTIGETEXECZ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIGETEXECZ) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIGETEXECZ = DICTIGETEXECZ
}