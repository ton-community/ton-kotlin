package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QMULDIVMOD : AsmInstruction, TlbConstructorProvider<QMULDIVMOD> by QMULDIVMODTlbConstructor {
    override fun toString(): String = "QMULDIVMOD"
}

private object QMULDIVMODTlbConstructor : TlbConstructor<QMULDIVMOD>(
    schema = "asm_qmuldivmod#b7a98c = QMULDIVMOD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QMULDIVMOD) {
    }

    override fun loadTlb(cellSlice: CellSlice): QMULDIVMOD = QMULDIVMOD
}