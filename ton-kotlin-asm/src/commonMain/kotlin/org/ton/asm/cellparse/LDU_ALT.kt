package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LDU_ALT(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc+1u} LDU_l"

    companion object : TlbConstructorProvider<LDU_ALT> by LDU_ALTTlbConstructor
}

private object LDU_ALTTlbConstructor : TlbConstructor<LDU_ALT>(
    schema = "asm_ldu_alt#d709 cc:uint8 = LDU_ALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDU_ALT) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LDU_ALT {
        val cc = cellSlice.loadUInt(8).toUByte()
        return LDU_ALT(cc)
    }
}
