package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class ROLL(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "${i + 1u} ROLL"

    companion object : TlbConstructorProvider<ROLL> by ROLLTlbConstructor
}

private object ROLLTlbConstructor : TlbConstructor<ROLL>(
    schema = "asm_roll#550 i:uint4 = ROLL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ROLL) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): ROLL {
        val i = cellSlice.loadUInt(4).toUByte()
        return ROLL(i)
    }
}
