package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SAVEBOTH(
    val c: Int
) : Instruction {
    override fun toString(): String = "c$c SAVEBOTH"

    companion object : TlbConstructorProvider<SAVEBOTH> by SAVEBOTHTlbConstructor
}

private object SAVEBOTHTlbConstructor : TlbConstructor<SAVEBOTH>(
    schema = "asm_saveboth#edc c:uint4 = SAVEBOTH;",
    type = SAVEBOTH::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SAVEBOTH) {
        cellBuilder.storeUInt(value.c, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SAVEBOTH {
        val c = cellSlice.loadUInt(4).toInt()
        return SAVEBOTH(c)
    }
}
