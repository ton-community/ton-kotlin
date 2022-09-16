package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SUBDICTIRPGET : AsmInstruction, TlbConstructorProvider<SUBDICTIRPGET> by SUBDICTIRPGETTlbConstructor {
    override fun toString(): String = "SUBDICTIRPGET"
}

private object SUBDICTIRPGETTlbConstructor : TlbConstructor<SUBDICTIRPGET>(
    schema = "asm_subdictirpget#f4b6 = SUBDICTIRPGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SUBDICTIRPGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): SUBDICTIRPGET = SUBDICTIRPGET
}