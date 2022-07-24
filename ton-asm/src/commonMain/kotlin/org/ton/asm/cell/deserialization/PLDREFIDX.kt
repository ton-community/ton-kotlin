package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PLDREFIDX(
    val n: Int
) : Instruction {
    override fun toString(): String = "$n PLDREFIDX"

    companion object : TlbConstructorProvider<PLDREFIDX> by PLDREFIDXTlbConstructor
}

private object PLDREFIDXTlbConstructor : TlbConstructor<PLDREFIDX>(
    schema = "asm_pldrefidx#d74e_ n:uint2 = PLDREFIDX;",
    type = PLDREFIDX::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDREFIDX) {
        cellBuilder.storeUInt(value.n, 2)
    }

    override fun loadTlb(cellSlice: CellSlice): PLDREFIDX {
        val n = cellSlice.loadUInt(2).toInt()
        return PLDREFIDX(n)
    }
}