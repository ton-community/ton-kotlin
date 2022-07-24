package org.ton.asm.flow.conditional

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class IFNBITJMPREF(
    val n: Int
) : Instruction {
    companion object : TlbConstructorProvider<IFNBITJMPREF> by IFNBITJMPREFTlbConstructor
}

private object IFNBITJMPREFTlbConstructor : TlbConstructor<IFNBITJMPREF>(
    schema = "asm_ifnbitjmpref#e3d_ n:uint32 = IFNBITJMPREF;",
    type = IFNBITJMPREF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNBITJMPREF) {
        cellBuilder.storeUInt(value.n, 32)
    }

    override fun loadTlb(cellSlice: CellSlice): IFNBITJMPREF {
        val n = cellSlice.loadUInt(32).toInt()
        return IFNBITJMPREF(n)
    }
}