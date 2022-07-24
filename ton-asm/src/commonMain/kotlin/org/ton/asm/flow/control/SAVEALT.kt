package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SAVEALT(
    val c: Int
) : Instruction {
    override fun toString(): String = "c$c SAVEALT"

    companion object : TlbConstructorProvider<SAVEALT> by SAVEALTTlbConstructor
}

private object SAVEALTTlbConstructor : TlbConstructor<SAVEALT>(
    schema = "asm_savealt#edb c:uint4 = SAVEALT;",
    type = SAVEALT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SAVEALT) {
        cellBuilder.storeUInt(value.c, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SAVEALT {
        val c = cellSlice.loadUInt(4).toInt()
        return SAVEALT(c)
    }
}