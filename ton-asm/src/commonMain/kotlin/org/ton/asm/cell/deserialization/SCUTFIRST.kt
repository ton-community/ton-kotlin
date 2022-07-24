package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCUTFIRST : Instruction, TlbConstructorProvider<SCUTFIRST> by SCUTFIRSTTlbConstructor {
    override fun toString(): String = "SCUTFIRST"
}

private object SCUTFIRSTTlbConstructor : TlbConstructor<SCUTFIRST>(
    schema = "asm_scutfirst#d730 = SCUTFIRST;",
    type = SCUTFIRST::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCUTFIRST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCUTFIRST = SCUTFIRST
}