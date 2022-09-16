package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SHA256U : AsmInstruction, TlbConstructorProvider<SHA256U> by SHA256UTlbConstructor {
    override fun toString(): String = "SHA256U"
}

private object SHA256UTlbConstructor : TlbConstructor<SHA256U>(
    schema = "asm_sha256u#f902 = SHA256U;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SHA256U) {
    }

    override fun loadTlb(cellSlice: CellSlice): SHA256U = SHA256U
}