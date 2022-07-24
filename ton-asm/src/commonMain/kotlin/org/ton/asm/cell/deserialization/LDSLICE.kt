package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDSLICE(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c LDSLICE"

    companion object : TlbConstructorProvider<LDSLICE> by LDSLICETlbConstructor
}

private object LDSLICETlbConstructor : TlbConstructor<LDSLICE>(
    schema = "asm_LDSLICE#d6 c:uint8 = LDSLICE;",
    type = LDSLICE::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDSLICE) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDSLICE {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return LDSLICE(c)
    }
}
