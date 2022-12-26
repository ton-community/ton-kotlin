package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class INDEXQ(
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "$k INDEXQ"

    companion object : TlbConstructorProvider<INDEXQ> by INDEXQTlbConstructor
}

private object INDEXQTlbConstructor : TlbConstructor<INDEXQ>(
    schema = "asm_indexq#6f6 k:uint4 = INDEXQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: INDEXQ) {
        cellBuilder.storeUInt(value.k.toLong(), 4)
    }

    override fun loadTlb(cellSlice: CellSlice): INDEXQ {
        val k = cellSlice.loadTinyInt(4).toUByte()
        return INDEXQ(k)
    }
}
