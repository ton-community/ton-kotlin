package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SAMEALTSAVE : Instruction, TlbConstructorProvider<SAMEALTSAVE> by SAMEALTSAVETlbConstructor {
    override fun toString(): String = "SAMEALTSAVE"
}

private object SAMEALTSAVETlbConstructor : TlbConstructor<SAMEALTSAVE>(
    schema = "asm_samealtsave#edfb = SAMEALTSAVE;",
    type = SAMEALTSAVE::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SAMEALTSAVE) {
    }

    override fun loadTlb(cellSlice: CellSlice): SAMEALTSAVE = SAMEALTSAVE
}