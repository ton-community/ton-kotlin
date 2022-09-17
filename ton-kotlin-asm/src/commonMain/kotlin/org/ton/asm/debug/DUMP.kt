package org.ton.asm.debug

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class DUMP(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i DUMP"

    companion object : TlbConstructorProvider<DUMP> by DUMPTlbConstructor
}

private object DUMPTlbConstructor : TlbConstructor<DUMP>(
    schema = "asm_dump#fe2 i:uint4 = DUMP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DUMP) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): DUMP {
        val i = cellSlice.loadUInt(4).toUByte()
        return DUMP(i)
    }
}
