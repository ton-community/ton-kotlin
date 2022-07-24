package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ISPOS : Instruction, TlbConstructorProvider<ISPOS> by ISPOSTlbConstructor {
    override fun toString(): String = "ISPOS"
}

private object ISPOSTlbConstructor : TlbConstructor<ISPOS>(
    schema = "asm_ispos#c200 = ISPOS;",
    type = ISPOS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ISPOS) {
    }

    override fun loadTlb(cellSlice: CellSlice): ISPOS = ISPOS
}