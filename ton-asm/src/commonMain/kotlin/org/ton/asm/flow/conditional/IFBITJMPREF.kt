package org.ton.asm.flow.conditional

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class IFBITJMPREF(
    val n: Int
) : Instruction {
    override fun toString(): String = "$n IFBITJMPREF"

    companion object : TlbConstructorProvider<IFBITJMPREF> by IFBITJMPREFTlbConstructor
}

private object IFBITJMPREFTlbConstructor : TlbConstructor<IFBITJMPREF>(
    schema = "asm_ifbitjmpref#e3d_ n:uint32 = IFBITJMPREF;",
    type = IFBITJMPREF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFBITJMPREF) {
        cellBuilder.storeUInt(value.n, 32)
    }

    override fun loadTlb(cellSlice: CellSlice): IFBITJMPREF {
        val n = cellSlice.loadUInt(32).toInt()
        return IFBITJMPREF(n)
    }
}