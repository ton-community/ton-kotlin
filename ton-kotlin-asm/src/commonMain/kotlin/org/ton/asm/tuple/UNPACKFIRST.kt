package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

public data class UNPACKFIRST(
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "$k UNPACKFIRST"

    companion object : TlbConstructorProvider<UNPACKFIRST> by UNPACKFIRSTTlbConstructor
}

private object UNPACKFIRSTTlbConstructor : TlbConstructor<UNPACKFIRST>(
    schema = "asm_unpackfirst#6f3 k:uint4 = UNPACKFIRST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UNPACKFIRST) {
        cellBuilder.storeUInt(value.k.toLong(), 4)
    }

    override fun loadTlb(cellSlice: CellSlice): UNPACKFIRST {
        val k = cellSlice.loadTinyInt(4).toUByte()
        return UNPACKFIRST(k)
    }
}
