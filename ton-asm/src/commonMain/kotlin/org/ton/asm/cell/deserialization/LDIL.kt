package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDIL(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c LDIL"

    companion object : TlbConstructorProvider<LDIL> by LDILTlbConstructor
}

private object LDILTlbConstructor : TlbConstructor<LDIL>(
    schema = "asm_ldil#d708 c:uint8 = LDIL;",
    type = LDIL::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDIL) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDIL {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return LDIL(c)
    }
}