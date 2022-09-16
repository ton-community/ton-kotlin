package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUGETJMPZ : AsmInstruction, TlbConstructorProvider<DICTUGETJMPZ> by DICTUGETJMPZTlbConstructor {
    override fun toString(): String = "DICTUGETJMPZ"
}

private object DICTUGETJMPZTlbConstructor : TlbConstructor<DICTUGETJMPZ>(
    schema = "asm_dictugetjmpz#f4bd = DICTUGETJMPZ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUGETJMPZ) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUGETJMPZ = DICTUGETJMPZ
}