package org.ton.asm.contdict

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUShort
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class CALLDICT_LONG(
    val n: UShort
) : AsmInstruction {
    override fun toString(): String = "$n CALLDICT"

    companion object : TlbConstructorProvider<CALLDICT_LONG> by CALLDICT_LONGTlbConstructor
}

private object CALLDICT_LONGTlbConstructor : TlbConstructor<CALLDICT_LONG>(
    schema = "asm_calldict_long#f12_ n:uint14 = CALLDICT_LONG;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CALLDICT_LONG) {
        cellBuilder.storeUInt(value.n, 14)
    }

    override fun loadTlb(cellSlice: CellSlice): CALLDICT_LONG {
        val n = cellSlice.loadUInt(14).toUShort()
        return CALLDICT_LONG(n)
    }
}
