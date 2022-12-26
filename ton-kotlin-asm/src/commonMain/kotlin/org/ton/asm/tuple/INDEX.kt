package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class INDEX(
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "$k INDEX"

    companion object : TlbConstructorProvider<INDEX> by INDEXTlbConstructor
}

private object INDEXTlbConstructor : TlbConstructor<INDEX>(
    schema = "asm_index#6f1 k:uint4 = INDEX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: INDEX) {
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): INDEX {
        val k = cellSlice.loadTinyInt(4).toUByte()
        return INDEX(k)
    }
}
