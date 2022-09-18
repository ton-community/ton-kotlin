package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.bigint.*
import org.ton.cell.Cell
import org.ton.tlb.providers.TlbConstructorProvider

data class IFBITJMPREF(
    var n: UByte,
    var ref: Cell
) : AsmInstruction {
    override fun toString(): String = "$ref $n IFBITJMPREF"

    companion object : TlbConstructorProvider<IFBITJMPREF> by IFBITJMPREFTlbConstructor
}

private object IFBITJMPREFTlbConstructor : TlbConstructor<IFBITJMPREF>(
    schema = "asm_ifbitjmpref#e3d_ n:uint5 c:^Cell = IFBITJMPREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFBITJMPREF) {
        cellBuilder.storeUInt(value.n, 5)
        cellBuilder.storeRef(value.ref)
    }

    override fun loadTlb(cellSlice: CellSlice): IFBITJMPREF {
        val n = cellSlice.loadUInt(5).toUByte()
        val ref = cellSlice.loadRef()
        return IFBITJMPREF(n, ref)
    }
}
