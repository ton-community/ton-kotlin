package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SAVEBOTH(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "c$i SAVEBOTH"

    companion object : TlbConstructorProvider<SAVEBOTH> by SAVEBOTHTlbConstructor
}

private object SAVEBOTHTlbConstructor : TlbConstructor<SAVEBOTH>(
    schema = "asm_saveboth#edc i:uint4 = SAVEBOTH;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SAVEBOTH) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SAVEBOTH {
        val i = cellSlice.loadUInt(4).toUByte()
        return SAVEBOTH(i)
    }
}
