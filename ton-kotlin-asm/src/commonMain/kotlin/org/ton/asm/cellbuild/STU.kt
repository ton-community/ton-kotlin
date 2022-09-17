package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class STU(
    val cc: UByte
) : AsmInstruction {
    override fun toString(): String = "${cc+1u} STU"

    companion object : TlbConstructorProvider<STU> by STUTlbConstructor
}

private object STUTlbConstructor : TlbConstructor<STU>(
    schema = "asm_stu#cb cc:uint8 = STU;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STU) {
        cellBuilder.storeUInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): STU {
        val cc = cellSlice.loadUInt(8).toUByte()
        return STU(cc)
    }
}
