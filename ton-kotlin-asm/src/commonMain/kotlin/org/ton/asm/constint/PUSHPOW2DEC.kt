package org.ton.asm.constint

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHPOW2DEC(
    val xx: UByte
) : AsmInstruction {
    override fun toString(): String = "${xx+1u} PUSHPOW2DEC"

    companion object : TlbConstructorProvider<PUSHPOW2DEC> by PUSHPOW2DECTlbConstructor
}

private object PUSHPOW2DECTlbConstructor : TlbConstructor<PUSHPOW2DEC>(
    schema = "asm_pushpow2dec#84 xx:uint8 = PUSHPOW2DEC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHPOW2DEC) {
        cellBuilder.storeUInt(value.xx, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHPOW2DEC {
        val xx = cellSlice.loadUInt(8).toUByte()
        return PUSHPOW2DEC(xx)
    }
}
