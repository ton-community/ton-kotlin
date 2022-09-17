package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class POPSAVE(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "c$i POPSAVE"

    companion object : TlbConstructorProvider<POPSAVE> by POPSAVETlbConstructor
}

private object POPSAVETlbConstructor : TlbConstructor<POPSAVE>(
    schema = "asm_popsave#ed9 i:uint4 = POPSAVE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: POPSAVE) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): POPSAVE {
        val i = cellSlice.loadUInt(4).toUByte()
        return POPSAVE(i)
    }
}
