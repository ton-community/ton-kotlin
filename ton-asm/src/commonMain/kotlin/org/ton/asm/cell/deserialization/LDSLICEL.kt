package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDSLICEL(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c LDSLICEL"

    companion object : TlbConstructorProvider<LDSLICEL> by LDSLICELTlbConstructor
}

private object LDSLICELTlbConstructor : TlbConstructor<LDSLICEL>(
    schema = "asm_ldslicel#d71c c:uint8 = LDSLICEL;",
    type = LDSLICEL::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDSLICEL) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDSLICEL {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return LDSLICEL(c)
    }
}