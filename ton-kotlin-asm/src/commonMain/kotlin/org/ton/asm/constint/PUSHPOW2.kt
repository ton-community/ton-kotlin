package org.ton.asm.constint

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHPOW2(
    val xx: UByte
) : AsmInstruction {
    override fun toString(): String = "${xx + 1u} PUSHPOW2"

    companion object : TlbConstructorProvider<PUSHPOW2> by PUSHPOW2TlbConstructor
}

private object PUSHPOW2TlbConstructor : TlbConstructor<PUSHPOW2>(
    schema = "asm_pushpow2#83 xx:uint8 = PUSHPOW2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHPOW2) {
        cellBuilder.storeUInt(value.xx, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHPOW2 {
        val xx = cellSlice.loadUInt(8).toUByte()
        return PUSHPOW2(xx)
    }
}
