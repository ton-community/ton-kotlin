package org.ton.asm.exceptions

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class THROWIFNOT_SHORT(
    val n: UByte
) : AsmInstruction {
    override fun toString(): String = "$n THROWIFNOT"

    companion object : TlbConstructorProvider<THROWIFNOT_SHORT> by THROWIFNOT_SHORTTlbConstructor
}

private object THROWIFNOT_SHORTTlbConstructor : TlbConstructor<THROWIFNOT_SHORT>(
    schema = "asm_throwifnot_short#f2a_ n:uint6 = THROWIFNOT_SHORT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWIFNOT_SHORT) {
        cellBuilder.storeUInt(value.n, 6)
    }

    override fun loadTlb(cellSlice: CellSlice): THROWIFNOT_SHORT {
        val n = cellSlice.loadUInt(6).toUByte()
        return THROWIFNOT_SHORT(n)
    }
}
