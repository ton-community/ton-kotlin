package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SETINDEX(
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "$k SETINDEX"

    companion object : TlbConstructorProvider<SETINDEX> by SETINDEXTlbConstructor
}

private object SETINDEXTlbConstructor : TlbConstructor<SETINDEX>(
    schema = "asm_setindex#6f5 k:uint4 = SETINDEX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETINDEX) {
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SETINDEX {
        val k = cellSlice.loadUInt(4).toUByte()
        return SETINDEX(k)
    }
}
