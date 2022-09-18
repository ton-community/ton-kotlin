package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class ROLLREV(
    val i: UByte,
    val zero: UByte = 0u
) : AsmInstruction {
    override fun toString(): String = "${i + 1u} ROLLREV"

    companion object : TlbConstructorProvider<ROLLREV> by ROLLREVTlbConstructor
}

private object ROLLREVTlbConstructor : TlbConstructor<ROLLREV>(
    schema = "asm_rollrev#55 i:uint4 zero:(## 4) {zero = 0} = ROLLREV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ROLLREV) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.zero, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): ROLLREV {
        val i = cellSlice.loadUInt(4).toUByte()
        val zero = cellSlice.loadUInt(4).toUByte()
        return ROLLREV(i, zero)
    }
}
