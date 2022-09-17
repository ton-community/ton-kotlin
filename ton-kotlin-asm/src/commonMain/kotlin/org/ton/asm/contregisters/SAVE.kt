package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SAVE(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "c$i SAVE"

    companion object : TlbConstructorProvider<SAVE> by SAVETlbConstructor
}

private object SAVETlbConstructor : TlbConstructor<SAVE>(
    schema = "asm_save#eda i:uint4 = SAVE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SAVE) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SAVE {
        val i = cellSlice.loadUInt(4).toUByte()
        return SAVE(i)
    }
}
