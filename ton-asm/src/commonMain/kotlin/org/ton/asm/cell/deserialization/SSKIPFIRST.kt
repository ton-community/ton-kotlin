package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SSKIPFIRST : Instruction, TlbConstructorProvider<SSKIPFIRST> by SSKIPFIRSTTlbConstructor {
    override fun toString(): String = "SSKIPFIRST"
}

private object SSKIPFIRSTTlbConstructor : TlbConstructor<SSKIPFIRST>(
    schema = "asm_sskipfirst#d731 = SSKIPFIRST;",
    type = SSKIPFIRST::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SSKIPFIRST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SSKIPFIRST = SSKIPFIRST
}