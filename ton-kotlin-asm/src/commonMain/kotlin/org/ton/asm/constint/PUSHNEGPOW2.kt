package org.ton.asm.constint

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHNEGPOW2(
    val xx: UByte
) : AsmInstruction {
    override fun toString(): String = "${xx+1u} PUSHNEGPOW2"

    companion object : TlbConstructorProvider<PUSHNEGPOW2> by PUSHNEGPOW2TlbConstructor
}

private object PUSHNEGPOW2TlbConstructor : TlbConstructor<PUSHNEGPOW2>(
    schema = "asm_pushnegpow2#85 xx:uint8 = PUSHNEGPOW2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHNEGPOW2) {
        cellBuilder.storeUInt(value.xx, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHNEGPOW2 {
        val xx = cellSlice.loadUInt(8).toUByte()
        return PUSHNEGPOW2(xx)
    }
}
