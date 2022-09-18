package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.bigint.*
import org.ton.tlb.providers.TlbConstructorProvider

data class XCHG_0I(
    val i: UByte
) : AsmInstruction {
    init {
        require(i >= 1u) { "expected i >= 1, actual: $i" }
    }

    override fun toString(): String = "s$i XCHG0"

    companion object : TlbConstructorProvider<XCHG_0I> by XCHG_0ITlbConstructor
}

private object XCHG_0ITlbConstructor : TlbConstructor<XCHG_0I>(
    schema = "asm_xchg_0i#0 i:(## 4) {1 <= i} = XCHG_0I;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHG_0I) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): XCHG_0I {
        val i = cellSlice.loadUInt(4).toUByte()
        return XCHG_0I(i)
    }
}
