package org.ton.asm.contconditional

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class IFNBITJMPREF(
    var n: UByte,
    var ref: Cell
) : AsmInstruction {
    override fun toString(): String = "$ref $n IFNBITJMPREF"

    companion object : TlbConstructorProvider<IFNBITJMPREF> by IFNBITJMPREFTlbConstructor
}

private object IFNBITJMPREFTlbConstructor : TlbConstructor<IFNBITJMPREF>(
    schema = "asm_ifnbitjmpref#e3f_ n:uint5 c:^Cell = IFNBITJMPREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNBITJMPREF) {
        cellBuilder.storeUInt(value.n, 5)
        cellBuilder.storeRef(value.ref)
    }

    override fun loadTlb(cellSlice: CellSlice): IFNBITJMPREF {
        val n = cellSlice.loadUInt(5).toUByte()
        val ref = cellSlice.loadRef()
        return IFNBITJMPREF(n, ref)
    }
}
