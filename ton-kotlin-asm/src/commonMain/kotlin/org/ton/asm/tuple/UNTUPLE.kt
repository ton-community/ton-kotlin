package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class UNTUPLE(
    val n: UByte
) : AsmInstruction {
    override fun toString(): String = "$n UNTUPLE"

    companion object : TlbConstructorProvider<UNTUPLE> by UNTUPLETlbConstructor
}

private object UNTUPLETlbConstructor : TlbConstructor<UNTUPLE>(
    schema = "asm_untuple#6f2 n:uint4 = UNTUPLE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UNTUPLE) {
        cellBuilder.storeUInt(value.n.toLong(), 4)
    }

    override fun loadTlb(cellSlice: CellSlice): UNTUPLE {
        val n = cellSlice.loadTinyInt(4).toUByte()
        return UNTUPLE(n)
    }
}
