package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SAVEALT(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "c$i SAVEALT"

    companion object : TlbConstructorProvider<SAVEALT> by SAVEALTTlbConstructor
}

private object SAVEALTTlbConstructor : TlbConstructor<SAVEALT>(
    schema = "asm_savealt#edb i:uint4 = SAVEALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SAVEALT) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SAVEALT {
        val i = cellSlice.loadUInt(4).toUByte()
        return SAVEALT(i)
    }
}
