package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDIQ(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c LDIQ"

    companion object : TlbConstructorProvider<LDIQ> by LDIQTlbConstructor
}

private object LDIQTlbConstructor : TlbConstructor<LDIQ>(
    schema = "asm_ldiq#d70c c:uint8 = LDIQ;",
    type = LDIQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDIQ) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDIQ {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return LDIQ(c)
    }
}