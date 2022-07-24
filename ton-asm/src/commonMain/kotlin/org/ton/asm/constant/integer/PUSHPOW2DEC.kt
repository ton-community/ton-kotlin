package org.ton.asm.constant.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHPOW2DEC(
    val x: Int
) : Instruction {
    override fun toString(): String = "$x PUSHPOW2DEC"

    companion object : TlbConstructorProvider<PUSHPOW2DEC> by PUSHPOW2DECTlbConstructor
}

private object PUSHPOW2DECTlbConstructor : TlbConstructor<PUSHPOW2DEC>(
    schema = "asm_pushpow2dec#84 x:uint8 = PUSHPOW2DEC;",
    type = PUSHPOW2DEC::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHPOW2DEC) {
        cellBuilder.storeUInt(value.x - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHPOW2DEC {
        val x = cellSlice.loadUInt(8).toInt() + 1
        return PUSHPOW2DEC(x)
    }
}