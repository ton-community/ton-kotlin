package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class XCHG_1I(
    val i: UByte,
) : AsmInstruction {
    init {
        require(i >= 2u) { "expected i >= 2, actual $i" }
    }

    override fun toString(): String = "s1 s$i XCHG"

    companion object : TlbConstructorProvider<XCHG_1I> by XCHG_1ITlbConstructor
}

private object XCHG_1ITlbConstructor : TlbConstructor<XCHG_1I>(
    schema = "asm_xchg_1i#1 i:(## 4) {2 <= i} = XCHG_1I;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHG_1I) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): XCHG_1I {
        val i = cellSlice.loadUInt(4).toUByte()
        return XCHG_1I(i)
    }
}