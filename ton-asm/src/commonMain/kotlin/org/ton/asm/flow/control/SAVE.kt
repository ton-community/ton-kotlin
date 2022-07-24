package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SAVE(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c SAVE"

    companion object : TlbConstructorProvider<SAVE> by SAVETlbConstructor
}

private object SAVETlbConstructor : TlbConstructor<SAVE>(
    schema = "asm_save#eda c:uint4 = SAVE;",
    type = SAVE::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SAVE) {
        cellBuilder.storeUInt(value.c, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SAVE {
        val c = cellSlice.loadUInt(4).toInt()
        return SAVE(c)
    }
}