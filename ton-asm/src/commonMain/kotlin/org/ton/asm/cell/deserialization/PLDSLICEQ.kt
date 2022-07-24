package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDSLICEQ(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c PLDSLICEQ"

    companion object : TlbConstructorProvider<PLDSLICEQ> by PLDSLICEQTlbConstructor
}

private object PLDSLICEQTlbConstructor : TlbConstructor<PLDSLICEQ>(
    schema = "asm_pldsliceq#d71f c:uint8 = PLDSLICEQ;",
    type = PLDSLICEQ::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDSLICEQ) {
        cellBuilder.storeUInt(value.c - 1, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDSLICEQ {
        val c = cellSlice.loadUInt(8).toInt() + 1
        return PLDSLICEQ(c)
    }
}