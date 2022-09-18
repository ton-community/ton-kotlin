package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDSLICEQ(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} LDSLICEQ"

    companion object : TlbConstructorProvider<LDSLICEQ> by LDSLICEQTlbConstructor
}

private object LDSLICEQTlbConstructor : TlbConstructor<LDSLICEQ>(
    schema = "asm_ldsliceq#d71e cc:uint8 = LDSLICEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDSLICEQ) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDSLICEQ {
        val cc = cellSlice.loadUInt(8).toUByte()
        return LDSLICEQ(cc)
    }
}
