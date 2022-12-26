package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDSLICEQ(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} PLDSLICEQ"

    companion object : TlbConstructorProvider<PLDSLICEQ> by PLDSLICEQTlbConstructor
}

private object PLDSLICEQTlbConstructor : TlbConstructor<PLDSLICEQ>(
    schema = "asm_pldsliceq#d71f cc:uint8 = PLDSLICEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDSLICEQ) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDSLICEQ {
        val cc = cellSlice.loadUInt(8).toUByte()
        return PLDSLICEQ(cc)
    }
}
