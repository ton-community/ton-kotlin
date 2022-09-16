package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object COMPOS : AsmInstruction, TlbConstructorProvider<COMPOS> by COMPOSTlbConstructor {
    override fun toString(): String = "COMPOS"
}

private object COMPOSTlbConstructor : TlbConstructor<COMPOS>(
    schema = "asm_compos#edf0 = COMPOS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: COMPOS) {
    }

    override fun loadTlb(cellSlice: CellSlice): COMPOS = COMPOS
}
