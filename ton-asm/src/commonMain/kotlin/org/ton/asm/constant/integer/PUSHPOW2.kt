package org.ton.asm.constant.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHPOW2(
    val x: Int
) : Instruction {
    override fun toString(): String = "$x PUSHPOW2"

    companion object : TlbConstructorProvider<PUSHPOW2> by PUSHPOW2TlbConstructor
}

private object PUSHPOW2TlbConstructor : TlbConstructor<PUSHPOW2>(
    schema = "asm_pushpow2#83 x:uint8 = PUSHPOW2;",
    type = PUSHPOW2::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHPOW2) {
        cellBuilder.storeUInt(value.x - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHPOW2 {
        val x = cellSlice.loadTinyInt(8).toInt() + 1
        return PUSHPOW2(x)
    }
}