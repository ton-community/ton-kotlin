package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class RETARGS(
    val r: UByte
) : AsmInstruction {
    override fun toString(): String = "$r RETARGS"

    companion object : TlbConstructorProvider<RETARGS> by RETARGSTlbConstructor
}

private object RETARGSTlbConstructor : TlbConstructor<RETARGS>(
    schema = "asm_retargs#db2 r:uint4 = RETARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RETARGS) {
        cellBuilder.storeUInt(value.r, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): RETARGS {
        val r = cellSlice.loadUInt(4).toUByte()
        return RETARGS(r)
    }
}
