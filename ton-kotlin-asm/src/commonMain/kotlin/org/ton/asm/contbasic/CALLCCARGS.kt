package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class CALLCCARGS(
    val p: UByte,
    val r: UByte
) : AsmInstruction {
    override fun toString(): String = "$p $r CALLCCARGS"

    companion object : TlbConstructorProvider<CALLCCARGS> by CALLCCARGSTlbConstructor
}

private object CALLCCARGSTlbConstructor : TlbConstructor<CALLCCARGS>(
    schema = "asm_callccargs#db36 p:uint4 r:uint4 = CALLCCARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CALLCCARGS) {
        cellBuilder.storeUInt(value.p, 4)
        cellBuilder.storeUInt(value.r, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): CALLCCARGS {
        val p = cellSlice.loadUInt(4).toUByte()
        val r = cellSlice.loadUInt(4).toUByte()
        return CALLCCARGS(p, r)
    }
}
