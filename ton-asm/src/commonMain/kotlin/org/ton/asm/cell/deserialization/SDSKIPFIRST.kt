package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDSKIPFIRST : Instruction, TlbConstructorProvider<SDSKIPFIRST> by SDSKIPFIRSTTlbConstructor {
    override fun toString(): String = "SDSKIPFIRST"
}

private object SDSKIPFIRSTTlbConstructor : TlbConstructor<SDSKIPFIRST>(
    schema = "asm_sdskipfirst#d721 = SDSKIPFIRST;",
    type = SDSKIPFIRST::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDSKIPFIRST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDSKIPFIRST = SDSKIPFIRST
}