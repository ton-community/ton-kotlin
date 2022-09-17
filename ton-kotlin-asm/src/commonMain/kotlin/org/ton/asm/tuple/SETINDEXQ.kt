package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SETINDEXQ(
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "$k SETINDEXQ"

    companion object : TlbConstructorProvider<SETINDEXQ> by SETINDEXQTlbConstructor
}

private object SETINDEXQTlbConstructor : TlbConstructor<SETINDEXQ>(
    schema = "asm_setindexq#6f7 k:uint4 = SETINDEXQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETINDEXQ) {
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SETINDEXQ {
        val k = cellSlice.loadUInt(4).toUByte()
        return SETINDEXQ(k)
    }
}
