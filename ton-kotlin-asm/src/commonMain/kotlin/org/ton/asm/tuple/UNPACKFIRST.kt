package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class UNPACKFIRST(
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "$k UNPACKFIRST"

    companion object : TlbConstructorProvider<UNPACKFIRST> by UNPACKFIRSTTlbConstructor
}

private object UNPACKFIRSTTlbConstructor : TlbConstructor<UNPACKFIRST>(
    schema = "asm_unpackfirst#6f3 k:uint4 = UNPACKFIRST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UNPACKFIRST) {
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): UNPACKFIRST {
        val k = cellSlice.loadUInt(4).toUByte()
        return UNPACKFIRST(k)
    }
}
