package org.ton.asm.constant.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHNEGPOW2(
    val x: Int
) : Instruction {
    override fun toString(): String = "$x PUSHNEGPOW2"

    companion object : TlbConstructorProvider<PUSHNEGPOW2> by PUSHNEGPOW2TlbConstructor
}

private object PUSHNEGPOW2TlbConstructor : TlbConstructor<PUSHNEGPOW2>(
    schema = "asm_pushnegpow2#85 x:uint8 = PUSHNEGPOW2;",
    type = PUSHNEGPOW2::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHNEGPOW2) {
        cellBuilder.storeUInt(value.x - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHNEGPOW2 {
        val x = cellSlice.loadTinyInt(8).toInt() + 1
        return PUSHNEGPOW2(x)
    }
}