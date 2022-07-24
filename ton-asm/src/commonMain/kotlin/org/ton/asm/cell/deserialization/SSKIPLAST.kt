package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SSKIPLAST : Instruction, TlbConstructorProvider<SSKIPLAST> by SSKIPLASTTlbConstructor {
    override fun toString(): String = "SSKIPLAST"
}

private object SSKIPLASTTlbConstructor : TlbConstructor<SSKIPLAST>(
    schema = "asm_sskiplast#d733 = SSKIPLAST;",
    type = SSKIPLAST::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SSKIPLAST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SSKIPLAST = SSKIPLAST
}