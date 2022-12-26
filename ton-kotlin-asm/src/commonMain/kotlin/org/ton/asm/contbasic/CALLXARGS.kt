package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class CALLXARGS(
    val p: UByte,
    val r: UByte
) : AsmInstruction {
    override fun toString(): String = "$p $r CALLXARGS"

    companion object : TlbConstructorProvider<CALLXARGS> by CALLXARGSTlbConstructor
}

private object CALLXARGSTlbConstructor : TlbConstructor<CALLXARGS>(
    schema = "asm_callxargs#da p:uint4 r:uint4 = CALLXARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CALLXARGS) {
        cellBuilder.storeUInt(value.p, 4)
        cellBuilder.storeUInt(value.r, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): CALLXARGS {
        val p = cellSlice.loadUInt(4).toUByte()
        val r = cellSlice.loadUInt(4).toUByte()
        return CALLXARGS(p, r)
    }
}
