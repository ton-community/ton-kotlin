package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDSLICEQ(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c LDSLICEQ"

    companion object : TlbConstructorProvider<LDSLICEQ> by LDSLICEQTlbConstructor
}

private object LDSLICEQTlbConstructor : TlbConstructor<LDSLICEQ>(
    schema = "asm_ldsliceq#d71e c:uint8 = LDSLICEQ;",
    type = LDSLICEQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDSLICEQ) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDSLICEQ {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return LDSLICEQ(c)
    }
}