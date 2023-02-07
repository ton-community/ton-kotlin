package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

public data class XCHG_0II(
    val ii: Int
) : AsmInstruction {
    override fun toString(): String = "s0 $ii s() XCHG"

    public companion object : TlbConstructorProvider<XCHG_0II> by XCHG_0IITlbConstructor
}

private object XCHG_0IITlbConstructor : TlbConstructor<XCHG_0II>(
    schema = "asm_xchg_0ii_long#11 ii:uint8 = XCHG_0II;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHG_0II) {
        cellBuilder.storeUInt(value.ii, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): XCHG_0II {
        val ii = cellSlice.loadUInt(8).toInt()
        return XCHG_0II(ii)
    }
}
