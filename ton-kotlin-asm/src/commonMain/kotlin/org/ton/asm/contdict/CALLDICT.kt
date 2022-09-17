package org.ton.asm.contdict

import org.ton.asm.AsmInstruction
import org.ton.bigint.*
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class CALLDICT(
    val nn: UByte
) : AsmInstruction {
    override fun toString(): String = "$nn CALLDICT"

    companion object : TlbConstructorProvider<CALLDICT> by CALLDICTTlbConstructor
}

private object CALLDICTTlbConstructor : TlbConstructor<CALLDICT>(
    schema = "asm_calldict#f0 n:uint8 = CALLDICT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CALLDICT) {
        cellBuilder.storeUInt(value.nn, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): CALLDICT {
        val n = cellSlice.loadUInt(8).toUByte()
        return CALLDICT(n)
    }
}
