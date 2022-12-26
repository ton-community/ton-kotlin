package org.ton.asm.exceptions

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUShort
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class THROWARGIFNOT(
    val n: UShort
) : AsmInstruction {
    override fun toString(): String = "$n THROWARGIFNOT"

    companion object : TlbConstructorProvider<THROWARGIFNOT> by THROWARGIFNOTTlbConstructor
}

private object THROWARGIFNOTTlbConstructor : TlbConstructor<THROWARGIFNOT>(
    schema = "asm_throwargifnot#f2ec_ n:uint11 = THROWARGIFNOT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWARGIFNOT) {
        cellBuilder.storeUInt(value.n, 11)
    }

    override fun loadTlb(cellSlice: CellSlice): THROWARGIFNOT {
        val n = cellSlice.loadUInt(11).toUShort()
        return THROWARGIFNOT(n)
    }
}
