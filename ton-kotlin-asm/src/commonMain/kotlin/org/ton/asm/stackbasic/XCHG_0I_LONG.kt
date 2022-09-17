package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class XCHG_0I_LONG(
    val ii: UByte
) : AsmInstruction {
    override fun toString(): String = "s0 $ii s() XCHG"

    companion object : TlbConstructorProvider<XCHG_0I_LONG> by XCHG_0I_LONGTlbConstructor
}

private object XCHG_0I_LONGTlbConstructor : TlbConstructor<XCHG_0I_LONG>(
    schema = "asm_xchg_0i_long#11 ii:uint8 = XCHG_0I_LONG;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHG_0I_LONG) {
        cellBuilder.storeUInt(value.ii, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): XCHG_0I_LONG {
        val ii = cellSlice.loadUInt(8).toUByte()
        return XCHG_0I_LONG(ii)
    }
}
