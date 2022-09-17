package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class REVERSE(
    val i: UByte,
    val j: UByte
) : AsmInstruction {
    override fun toString(): String = "${i+2u} $j REVERSE"

    companion object : TlbConstructorProvider<REVERSE> by REVERSETlbConstructor
}

private object REVERSETlbConstructor : TlbConstructor<REVERSE>(
    schema = "asm_reverse#5e i:uint4 j:uint4 = REVERSE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: REVERSE) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): REVERSE {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        return REVERSE(i, j)
    }
}
