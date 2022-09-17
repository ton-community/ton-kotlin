package org.ton.asm.exceptions

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class THROW_SHORT(
    val n: UByte
) : AsmInstruction {
    override fun toString(): String = "$n THROW"

    companion object : TlbConstructorProvider<THROW_SHORT> by THROW_SHORTTlbConstructor
}

private object THROW_SHORTTlbConstructor : TlbConstructor<THROW_SHORT>(
    schema = "asm_throw_short#f22_ n:uint6 = THROW_SHORT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROW_SHORT) {
        cellBuilder.storeUInt(value.n, 6)
    }

    override fun loadTlb(cellSlice: CellSlice): THROW_SHORT {
        val n = cellSlice.loadUInt(6).toUByte()
        return THROW_SHORT(n)
    }
}
