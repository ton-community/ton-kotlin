package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QUFITSX : AsmInstruction, TlbConstructorProvider<QUFITSX> by QUFITSXTlbConstructor {
    override fun toString(): String = "QUFITSX"
}

private object QUFITSXTlbConstructor : TlbConstructor<QUFITSX>(
    schema = "asm_qufitsx#b7b601 = QUFITSX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QUFITSX) {
    }

    override fun loadTlb(cellSlice: CellSlice): QUFITSX = QUFITSX
}