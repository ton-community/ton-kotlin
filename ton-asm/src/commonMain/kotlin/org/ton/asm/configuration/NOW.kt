package org.ton.asm.configuration

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NOW : Instruction, TlbConstructorProvider<NOW> by NOWTlbConstructor {
    override fun toString(): String = "NOW"
}

private object NOWTlbConstructor : TlbConstructor<NOW>(
    schema = "asm_now#fb23 = NOW;",
    type = NOW::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NOW) {
    }

    override fun loadTlb(cellSlice: CellSlice): NOW = NOW
}