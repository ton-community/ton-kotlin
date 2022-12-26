package org.ton.asm.exceptions

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUShort
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class THROWARG(
    val n: UShort
) : AsmInstruction {
    override fun toString(): String = "$n THROWARG"

    companion object : TlbConstructorProvider<THROWARG> by THROWARGTlbConstructor
}

private object THROWARGTlbConstructor : TlbConstructor<THROWARG>(
    schema = "asm_throwarg#f2cc_ n:uint11 = THROWARG;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THROWARG) {
        cellBuilder.storeUInt(value.n, 11)
    }

    override fun loadTlb(cellSlice: CellSlice): THROWARG {
        val n = cellSlice.loadUInt(11).toUShort()
        return THROWARG(n)
    }
}
