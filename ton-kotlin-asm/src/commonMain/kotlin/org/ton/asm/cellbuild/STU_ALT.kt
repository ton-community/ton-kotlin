package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class STU_ALT(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc + 1u} STU_l"

    companion object : TlbConstructorProvider<STU_ALT> by STU_ALTTlbConstructor
}

private object STU_ALTTlbConstructor : TlbConstructor<STU_ALT>(
    schema = "asm_stu_alt#cf09 cc:uint8 = STU_ALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STU_ALT) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): STU_ALT {
        val cc = cellSlice.loadUInt(8).toUByte()
        return STU_ALT(cc)
    }
}
