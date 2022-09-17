package org.ton.asm.exceptions

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class THROWIF_SHORT(
    val n: UByte
) : AsmInstruction {
    override fun toString(): String = "$n THROWIF"

    companion object : TlbConstructorProvider<THROWIF_SHORT> by THROWIF_SHORTTlbConstructor
}

private object THROWIF_SHORTTlbConstructor : TlbConstructor<THROWIF_SHORT>(
    schema = "asm_throwif_short#f26_ n:uint6 = THROWIF_SHORT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWIF_SHORT) {
        cellBuilder.storeUInt(value.n, 6)
    }

    override fun loadTlb(cellSlice: CellSlice): THROWIF_SHORT {
        val n = cellSlice.loadUInt(6).toUByte()
        return THROWIF_SHORT(n)
    }
}
