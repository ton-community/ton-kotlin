package org.ton.asm.dictspecial

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIGETJMPZ : AsmInstruction, TlbConstructorProvider<DICTIGETJMPZ> by DICTIGETJMPZTlbConstructor {
    override fun toString(): String = "DICTIGETJMPZ"
}

private object DICTIGETJMPZTlbConstructor : TlbConstructor<DICTIGETJMPZ>(
    schema = "asm_dictigetjmpz#f4bc = DICTIGETJMPZ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIGETJMPZ) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIGETJMPZ = DICTIGETJMPZ
}
