package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class STREF2CONST(
    val c1: Cell,
    val c2: Cell
) : AsmInstruction {
    override fun toString(): String = "$c1 $c2 STREF2CONST"

    companion object : TlbConstructorProvider<STREF2CONST> by STREF2CONSTTlbConstructor
}

private object STREF2CONSTTlbConstructor : TlbConstructor<STREF2CONST>(
    schema = "asm_stref2const#cf21 c1:^Cell c2:^Cell = STREF2CONST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STREF2CONST) {
        cellBuilder.storeRef(value.c1)
        cellBuilder.storeRef(value.c2)
    }

    override fun loadTlb(cellSlice: CellSlice): STREF2CONST {
        val c1 = cellSlice.loadRef()
        val c2 = cellSlice.loadRef()
        return STREF2CONST(c1, c2)
    }
}
